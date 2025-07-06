package com.tp1202510030.backend.growrooms.domain.model.aggregates;

import com.tp1202510030.backend.companies.domain.model.aggregates.Company;
import com.tp1202510030.backend.growrooms.domain.model.entities.Measurement;
import com.tp1202510030.backend.growrooms.domain.model.valueobjects.ActuatorType;
import com.tp1202510030.backend.growrooms.domain.model.valueobjects.ControlActionType;
import com.tp1202510030.backend.growrooms.domain.model.valueobjects.GrowRoomName;
import com.tp1202510030.backend.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EntityListeners(AuditingEntityListener.class)
@Entity
public class GrowRoom extends AuditableAbstractAggregateRoot<GrowRoom> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Embedded
    private GrowRoomName name;

    @Getter
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    @Getter
    private Company company;

    @Getter
    @Setter
    @Transient
    private List<Measurement> latestMeasurements = new ArrayList<>();

    @Getter
    @Setter
    @Transient
    private Map<ActuatorType, ControlActionType> actuatorStates = new HashMap<>();

    @Getter
    @Setter
    @Transient
    private Long activeCropId;

    private boolean hasActiveCrop;

    public GrowRoom() {
    }

    public GrowRoom(String name, String imageUrl, Company company) {
        this.name = new GrowRoomName(name);
        this.imageUrl = imageUrl;
        this.company = company;
        this.hasActiveCrop = false;
    }

    /**
     * Update grow room information.
     *
     * @param name     Grow room name.
     * @param imageUrl Grow room image.
     * @param company  The company that owns the grow room.
     * @return Grow room instance.
     */
    public GrowRoom updateInformation(String name, String imageUrl, Company company) {
        this.name = new GrowRoomName(name);
        this.imageUrl = imageUrl;
        this.company = company;
        return this;
    }

    public String getGrowRoomName() {
        return this.name.name();
    }

    public boolean getHasActiveCrop() {
        return this.hasActiveCrop;
    }

    public void activateCrop() {
        this.hasActiveCrop = true;
    }

    public void deactivateCrop() {
        this.hasActiveCrop = false;
    }
}
