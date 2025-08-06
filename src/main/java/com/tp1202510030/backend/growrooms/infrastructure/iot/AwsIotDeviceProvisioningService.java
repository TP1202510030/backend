package com.tp1202510030.backend.growrooms.infrastructure.iot;

import com.tp1202510030.backend.growrooms.domain.model.valueobjects.DeviceCredentials;
import com.tp1202510030.backend.growrooms.domain.services.iot.IotDeviceProvisioningService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.iot.IotClient;
import software.amazon.awssdk.services.iot.model.CreateKeysAndCertificateResponse;
import software.amazon.awssdk.services.iot.model.CreatePolicyRequest;
import software.amazon.awssdk.services.iot.model.CreateThingRequest;
import software.amazon.awssdk.services.iot.model.IotException;

import java.util.Optional;

@Service
public class AwsIotDeviceProvisioningService implements IotDeviceProvisioningService {
    private final IotClient iotClient;

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
            System.err.println("Failed to provision device in AWS IoT: " + e.getMessage());
            return Optional.empty();
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
