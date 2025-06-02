package com.tp1202510030.backend.growrooms.domain.model.entities;

import com.tp1202510030.backend.growrooms.domain.model.valueobjects.Parameters;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
public class Measurement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter
    private Parameters parameter;

    @Getter
    private Double value;

    @Getter
    private Instant timestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crop_phase_id", nullable = false)
    private CropPhase cropPhase;

    public Measurement(Parameters parameter, Double value, Instant timestamp, CropPhase cropPhase) {
        this.parameter = parameter;
        this.value = value;
        this.timestamp = timestamp;
        this.cropPhase = cropPhase;
    }
}
