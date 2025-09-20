package com.tp1202510030.backend.growrooms.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class CropFinishedEvent extends ApplicationEvent {
    private final Long growRoomId;

    public CropFinishedEvent(Object source, Long growRoomId) {
        super(source);
        this.growRoomId = growRoomId;
    }
}