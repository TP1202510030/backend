package com.tp1202510030.backend.companies.application.internal.outboundservices.acl;

import com.tp1202510030.backend.growrooms.interfaces.acl.GrowRoomContextFacade;
import org.springframework.stereotype.Service;

@Service
public class ExternalGrowRoomService {
    private final GrowRoomContextFacade growRoomContextFacade;

    public ExternalGrowRoomService(GrowRoomContextFacade growRoomContextFacade) {
        this.growRoomContextFacade = growRoomContextFacade;
    }

    public void deleteAllGrowRoomsByCompanyId(Long companyId) {
        growRoomContextFacade.deleteAllGrowRoomsByCompanyId(companyId);
    }

}
