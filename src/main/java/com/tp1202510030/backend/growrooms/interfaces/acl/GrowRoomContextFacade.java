package com.tp1202510030.backend.growrooms.interfaces.acl;

import org.springframework.stereotype.Service;

@Service
public interface GrowRoomContextFacade {
    /**
     * Delete a grow room by its ID.
     *
     * @param growRoomId The grow room ID
     */
    void deleteGrowRoomById(Long growRoomId);

    /**
     * Deletes all grow rooms by a company ID
     *
     * @param companyId The company ID
     */
    void deleteAllGrowRoomsByCompanyId(Long companyId);
}
