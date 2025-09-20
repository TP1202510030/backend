package com.tp1202510030.backend.growrooms.infrastructure.iot;

import com.tp1202510030.backend.growrooms.domain.model.valueobjects.DeviceCredentials;
import com.tp1202510030.backend.growrooms.domain.services.iot.IotDeviceProvisioningService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.iot.IotClient;
import software.amazon.awssdk.services.iot.model.*;

import java.util.List;
import java.util.Optional;

@Service
public class AwsIotDeviceProvisioningService implements IotDeviceProvisioningService {
    private final IotClient iotClient;
    private static final Logger logger = LoggerFactory.getLogger(AwsIotDeviceProvisioningService.class);

    @Value("${aws.iot.account-id}")
    private String awsAccountId;

    @Value("${aws.iot.region}")
    private String awsRegion;

    public AwsIotDeviceProvisioningService(IotClient iotClient) {
        this.iotClient = iotClient;
    }

    @Override
    public Optional<DeviceCredentials> provisionDevice(Long companyId, Long growRoomId) {
        try {
            String thingName = "company-%d-growroom-%d".formatted(companyId, growRoomId);
            iotClient.createThing(CreateThingRequest.builder().thingName(thingName).build());

            String policyName = thingName + "-Policy";
            String policyDocument = buildDevicePolicy(companyId, growRoomId, thingName);
            iotClient.createPolicy(CreatePolicyRequest.builder()
                    .policyName(policyName)
                    .policyDocument(policyDocument)
                    .build());

            CreateKeysAndCertificateResponse certResponse = iotClient.createKeysAndCertificate(req -> req.setAsActive(true));
            String certificateArn = certResponse.certificateArn();

            iotClient.attachPolicy(req -> req.policyName(policyName).target(certificateArn));
            iotClient.attachThingPrincipal(req -> req.thingName(thingName).principal(certificateArn));

            return Optional.of(new DeviceCredentials(
                    thingName,
                    certResponse.certificatePem(),
                    certResponse.keyPair().privateKey(),
                    certificateArn
            ));
        } catch (IotException e) {
            logger.error("Failed to provision device in AWS IoT. Error: {}", e.getMessage());
            throw new RuntimeException("Failed to provision device in AWS IoT: " + e.getMessage(), e);
        }
    }

    @Override
    public void deprovisionDevice(Long companyId, Long growRoomId) {
        String thingName = "company-%d-growroom-%d".formatted(companyId, growRoomId);
        String policyName = thingName + "-Policy";
        logger.info("Starting deprovisioning for IoT Thing: {}", thingName);

        try {
            List<String> principals = iotClient.listThingPrincipals(req -> req.thingName(thingName)).principals();

            for (String principalArn : principals) {
                logger.info("Detaching principal {} from thing {}", principalArn, thingName);
                iotClient.detachThingPrincipal(req -> req.thingName(thingName).principal(principalArn));

                String certificateId = principalArn.substring(principalArn.lastIndexOf('/') + 1);

                logger.info("Detaching policy {} from principal {}", policyName, principalArn);
                iotClient.detachPolicy(req -> req.policyName(policyName).target(principalArn));

                logger.info("Deactivating certificate {}", certificateId);
                iotClient.updateCertificate(req -> req.certificateId(certificateId).newStatus(CertificateStatus.INACTIVE));

                logger.info("Deleting certificate {}", certificateId);
                iotClient.deleteCertificate(req -> req.certificateId(certificateId));
            }

            logger.info("Deleting policy {}", policyName);
            iotClient.deletePolicy(req -> req.policyName(policyName));

            logger.info("Deleting thing {}", thingName);
            iotClient.deleteThing(req -> req.thingName(thingName));

            logger.info("Successfully deprovisioned IoT Thing: {}", thingName);

        } catch (ResourceNotFoundException e) {
            logger.warn("A resource was not found during deprovisioning for thing {}. It might have been already deleted. Message: {}", thingName, e.getMessage());
        } catch (IotException e) {
            logger.error("Failed to deprovision device in AWS IoT for thing: {}. Error: {}", thingName, e.awsErrorDetails().errorMessage());
            throw new RuntimeException("Failed to deprovision device in AWS IoT: " + e.awsErrorDetails().errorMessage(), e);
        }
    }

    private String buildDevicePolicy(Long companyId, Long growRoomId, String thingName) {
        String measurementsTopic = "arn:aws:iot:%s:%s:topic/%d/%d/measurements".formatted(awsRegion, awsAccountId, companyId, growRoomId);
        String clientArn = "arn:aws:iot:%s:%s:client/%s".formatted(awsRegion, awsAccountId, thingName);

        return """
                {
                  "Version": "2012-10-17",
                  "Statement": [
                    {
                      "Effect": "Allow",
                      "Action": "iot:Connect",
                      "Resource": "%s"
                    },
                    {
                      "Effect": "Allow",
                      "Action": "iot:Publish",
                      "Resource": "%s"
                    }
                  ]
                }
                """.formatted(clientArn, measurementsTopic);
    }
}
