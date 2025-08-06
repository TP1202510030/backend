package com.tp1202510030.backend.shared.application.internal.services;

import com.tp1202510030.backend.growrooms.infrastructure.persistence.jpa.repositories.CropPhaseRepository;
import com.tp1202510030.backend.growrooms.infrastructure.persistence.jpa.repositories.CropRepository;
import com.tp1202510030.backend.growrooms.infrastructure.persistence.jpa.repositories.GrowRoomRepository;
import com.tp1202510030.backend.iam.infrastructure.authorization.sfs.model.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service("securityService")
public class SecurityService {

    private final GrowRoomRepository growRoomRepository;
    private final CropRepository cropRepository;
    private final CropPhaseRepository cropPhaseRepository;

    public SecurityService(GrowRoomRepository growRoomRepository, CropRepository cropRepository, CropPhaseRepository cropPhaseRepository) {
        this.growRoomRepository = growRoomRepository;
        this.cropRepository = cropRepository;
        this.cropPhaseRepository = cropPhaseRepository;
    }

    private UserDetailsImpl getPrincipal(Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null || !(authentication.getPrincipal() instanceof UserDetailsImpl)) {
            return null;
        }
        return (UserDetailsImpl) authentication.getPrincipal();
    }

    public boolean isResourceOwner(Authentication authentication, Long companyId) {
        UserDetailsImpl principal = getPrincipal(authentication);
        if (principal == null || principal.getCompanyId() == null) return false;
        return Objects.equals(principal.getCompanyId(), companyId);
    }

    public boolean isGrowRoomOwner(Authentication authentication, Long growRoomId) {
        UserDetailsImpl principal = getPrincipal(authentication);
        if (principal == null || principal.getCompanyId() == null) return false;

        return growRoomRepository.findById(growRoomId)
                .map(growRoom -> growRoom.getCompany().getId().equals(principal.getCompanyId()))
                .orElse(true); // Let the controller handle the 404
    }

    public boolean isCropOwner(Authentication authentication, Long cropId) {
        UserDetailsImpl principal = getPrincipal(authentication);
        if (principal == null || principal.getCompanyId() == null) return false;

        return cropRepository.findById(cropId)
                .map(crop -> crop.getGrowRoom().getCompany().getId().equals(principal.getCompanyId()))
                .orElse(true);
    }

    public boolean isCropPhaseOwner(Authentication authentication, Long cropPhaseId) {
        UserDetailsImpl principal = getPrincipal(authentication);
        if (principal == null || principal.getCompanyId() == null) return false;

        return cropPhaseRepository.findById(cropPhaseId)
                .map(phase -> phase.getCrop().getGrowRoom().getCompany().getId().equals(principal.getCompanyId()))
                .orElse(true);
    }
}