package com.tp1202510030.backend.growrooms.domain.services.iot;

import com.tp1202510030.backend.growrooms.domain.model.valueobjects.DeviceCredentials;

import java.util.Optional;

public interface IotDeviceProvisioningService {
    Optional<DeviceCredentials> provisionDevice(Long companyId, Long growRoomId);

    /**
     * Deprovisions a device from AWS IoT.
     * This includes detaching and deleting policies, certificates, and the thing itself.
     *
     * @param companyId  The ID of the company owning the grow room.
     * @param growRoomId The ID of the grow room.
     * @throws RuntimeException if the deprovisioning process fails.
     */
    void deprovisionDevice(Long companyId, Long growRoomId);
}
