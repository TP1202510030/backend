package com.tp1202510030.backend.growrooms.domain.model.aggregates;

import com.tp1202510030.backend.companies.domain.model.aggregates.Company;
import com.tp1202510030.backend.growrooms.domain.model.entities.Measurement;
import com.tp1202510030.backend.growrooms.domain.model.valueobjects.GrowRoomName;
import com.tp1202510030.backend.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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

    @OneToMany(mappedBy = "growRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter
    private java.util.List<Crop> crops;

    @Transient
    @Setter
    @Getter
    private Measurement latestMeasurement;

    public GrowRoom() {
    }

    public GrowRoom(String name, String imageUrl, Company company) {
        this.name = new GrowRoomName(name);
        this.imageUrl = imageUrl;
        this.company = company;
    }

    /**
     * Update grow room information.
     *
     * @param name     Grow room name.
     * @param imageUrl Grow room image.
     * @param company  The company that owns the grow room.
     * @return Grow room instance.
     */
    public GrowRoom updateInformation(GrowRoomName name, String imageUrl, Company company) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.company = company;
        return this;
    }

    public String getGrowRoomName() {
        return this.name.name();
    }
}
