package com.tp1202510030.backend.growrooms.domain.model.valueobjects;

public record DeviceCredentials(
        String thingName,
        String certificatePem,
        String privateKey,
        String certificateArn
) {
}
