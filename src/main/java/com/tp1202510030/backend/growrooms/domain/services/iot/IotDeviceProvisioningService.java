package com.tp1202510030.backend.growrooms.domain.services.iot;

import com.tp1202510030.backend.growrooms.domain.model.valueobjects.DeviceCredentials;

import java.util.Optional;

public interface IotDeviceProvisioningService {
    Optional<DeviceCredentials> provisionDevice(Long companyId, Long growRoomId);
}
