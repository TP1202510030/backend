package com.tp1202510030.backend.growrooms.domain.model.entities;

import com.tp1202510030.backend.growrooms.domain.model.valueobjects.ActuatorType;
import com.tp1202510030.backend.growrooms.domain.model.valueobjects.ControlActionType;
import com.tp1202510030.backend.growrooms.domain.model.valueobjects.Parameters;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class ControlAction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter
    private ActuatorType actuatorType;

    @Getter
    private ControlActionType controlActionType;

    @Getter
    private String triggeringReason;

    @Getter
    private Parameters triggeringParameterType;

    @Getter
    private Double triggeringMeasurementValue;

    @Getter
    private Date timestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crop_phase_id", nullable = false)
    private CropPhase cropPhase;

    public ControlAction(ActuatorType actuatorType,
                         ControlActionType controlActionType,
                         String triggeringReason,
                         Parameters triggeringParameterType,
                         Double triggeringMeasurementValue,
                         CropPhase cropPhase) {
        this.actuatorType = actuatorType;
        this.controlActionType = controlActionType;
        this.triggeringReason = triggeringReason;
        this.triggeringParameterType = triggeringParameterType;
        this.triggeringMeasurementValue = triggeringMeasurementValue;
        this.timestamp = new Date();
        this.cropPhase = cropPhase;
    }
}
