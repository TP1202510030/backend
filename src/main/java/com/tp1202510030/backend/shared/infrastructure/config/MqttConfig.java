package com.tp1202510030.backend.shared.infrastructure.config;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.io.FileReader;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.Security;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;

@Configuration
public class MqttConfig {

    @Value("${MQTT_BROKER_URL}")
    private String brokerUrl;

    @Value("${MQTT_CLIENT_ID}")
    private String clientId;

    @Value("${MQTT_CA_CERTIFICATE_PATH}")
    private String caCertificatePath;

    @Value("${MQTT_CLIENT_CERTIFICATE_PATH}")
    private String clientCertificatePath;

    @Value("${MQTT_CLIENT_PRIVATE_KEY_PATH}")
    private String privateKeyPath;

    @Bean
    public MqttPahoClientFactory mqttClientFactory() throws Exception {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(new String[]{brokerUrl});
        options.setSocketFactory(sslSocketFactory());
        factory.setConnectionOptions(options);
        return factory;
    }

    @Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutbound() throws Exception {
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(clientId, mqttClientFactory());
        messageHandler.setAsync(true);
        messageHandler.setDefaultTopic("default-topic"); // Un topic por defecto, se puede sobreescribir
        return messageHandler;
    }

    private SSLSocketFactory sslSocketFactory() throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        // --- Cargar el certificado de la CA (para confiar en AWS) ---
        CertificateFactory cAf = CertificateFactory.getInstance("X.509");
        // CORRECCIÓN AQUÍ: Usa FileInputStream
        X509Certificate caCert = (X509Certificate) cAf.generateCertificate(new FileInputStream(caCertificatePath));
        KeyStore caKs = KeyStore.getInstance(KeyStore.getDefaultType());
        caKs.load(null, null);
        caKs.setCertificateEntry("ca-certificate", caCert);
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(caKs);

        // --- Cargar el certificado y la clave del cliente (para que AWS confíe en nosotros) ---
        CertificateFactory cF = CertificateFactory.getInstance("X.509");
        // CORRECCIÓN AQUÍ: Usa FileInputStream
        X509Certificate clientCert = (X509Certificate) cF.generateCertificate(new FileInputStream(clientCertificatePath));

        // Cargar clave privada usando Bouncy Castle
        // CORRECCIÓN AQUÍ: Usa FileInputStream, que es compatible con Reader a través de un wrapper si es necesario, pero aquí FileReader era incorrecto.
        PEMParser pemParser = new PEMParser(new FileReader(privateKeyPath)); // PEMParser específicamente quiere un Reader, así que aquí FileReader SÍ es correcto.
        Object object = pemParser.readObject();
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");
        KeyPair keyPair = converter.getKeyPair((PEMKeyPair) object);

        KeyStore clientKs = KeyStore.getInstance(KeyStore.getDefaultType());
        clientKs.load(null, null);
        clientKs.setCertificateEntry("certificate", clientCert);
        clientKs.setKeyEntry("private-key", keyPair.getPrivate(), "".toCharArray(), new java.security.cert.Certificate[]{clientCert});

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(clientKs, "".toCharArray());

        // --- Crear el contexto SSL con ambos KeyStores ---
        SSLContext context = SSLContext.getInstance("TLSv1.2");
        context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        return context.getSocketFactory();
    }

    @MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
    public interface MqttGateway {
        void sendToMqtt(@Payload String payload,@Header(MqttHeaders.TOPIC) String topic);
    }
}