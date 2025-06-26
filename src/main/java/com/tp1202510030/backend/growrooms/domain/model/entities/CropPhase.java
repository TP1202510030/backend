package com.tp1202510030.backend.growrooms.domain.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tp1202510030.backend.growrooms.domain.model.aggregates.Crop;
import com.tp1202510030.backend.growrooms.domain.model.valueobjects.GrowPhaseName;
import com.tp1202510030.backend.growrooms.domain.model.valueobjects.ParameterThresholds;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class CropPhase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter
    private GrowPhaseName name;

    @Getter
    private Duration duration;

    @Embedded
    @Getter
    private ParameterThresholds thresholds;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crop_id", nullable = false)
    @Setter
    @JsonIgnore
    private Crop crop;

    @OneToMany(mappedBy = "cropPhase", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Measurement> measurements = new ArrayList<>();

    @OneToMany(mappedBy = "cropPhase", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ControlAction> controlActions = new ArrayList<>();

    public CropPhase(String name, Duration duration, ParameterThresholds thresholds) {
        this.name = new GrowPhaseName(name);
        this.duration = duration;
        this.thresholds = thresholds;
    }
}
