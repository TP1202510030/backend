package com.tp1202510030.backend.growrooms.interfaces.rest.resources.growroom;

public record DeviceCredentialsResource(
        String thingName,
        String certificatePem,
        String privateKey
) {
}
