package com.tp1202510030.backend.growrooms.interfaces.rest.transform.growroom;

import com.tp1202510030.backend.growrooms.domain.model.valueobjects.DeviceCredentials;
import com.tp1202510030.backend.growrooms.interfaces.rest.resources.growroom.DeviceCredentialsResource;

public class DeviceCredentialsResourceFromDomainAssembler {
    public static DeviceCredentialsResource toResourceFromDomain(DeviceCredentials credentials) {
        return new DeviceCredentialsResource(
                credentials.thingName(),
                credentials.certificatePem(),
                credentials.privateKey()
        );
    }
}