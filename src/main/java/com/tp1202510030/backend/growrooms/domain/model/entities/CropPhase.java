package com.tp1202510030.backend.growrooms.domain.model.entities;

import com.tp1202510030.backend.growrooms.domain.model.aggregates.Crop;
import com.tp1202510030.backend.growrooms.domain.model.valueobjects.GrowPhaseName;
import com.tp1202510030.backend.growrooms.domain.model.valueobjects.ParameterThresholds;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
public class CropPhase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter
    @Setter
    private GrowPhaseName name;

    @Getter
    @Setter
    private Integer durationInDays;

    @Embedded
    @Getter
    @Setter
    private ParameterThresholds thresholds;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crop_id")
    private Crop crop;

    public CropPhase() {}

    public CropPhase(String name, Integer durationInDays, ParameterThresholds thresholds) {
        this.name = new GrowPhaseName(name);
        this.durationInDays = durationInDays;
        this.thresholds = thresholds;
    }
}
