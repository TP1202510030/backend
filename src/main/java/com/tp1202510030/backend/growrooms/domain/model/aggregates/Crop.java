package com.tp1202510030.backend.growrooms.domain.model.aggregates;

import com.tp1202510030.backend.growrooms.domain.model.entities.CropPhase;
import com.tp1202510030.backend.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Duration;
import java.util.Date;
import java.util.List;

@EntityListeners(AuditingEntityListener.class)
@Entity
public class Crop extends AuditableAbstractAggregateRoot<Crop> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter
    private Date startDate;

    @Getter
    @Setter
    private Date endDate;

    @Getter
    private Duration sensorActivationFrequency;

    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grow_room_id")
    private GrowRoom growRoom;

    @Getter
    @OneToMany(mappedBy = "crop", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CropPhase> phases;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_phase_id")
    @Getter
    @Setter
    private CropPhase currentPhase;

    public Crop() {
    }

    public Crop(Date startDate, Date endDate, Duration sensorActivationFrequency, GrowRoom growRoom, List<CropPhase> phases) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.sensorActivationFrequency = sensorActivationFrequency;
        this.growRoom = growRoom;
        this.phases = phases;
        this.currentPhase = null;
    }

    public void updateCurrentPhase(CropPhase phase) {
        this.currentPhase = phase;
    }
}
