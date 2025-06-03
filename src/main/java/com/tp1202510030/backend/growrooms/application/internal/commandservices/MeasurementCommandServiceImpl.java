package com.tp1202510030.backend.growrooms.application.internal.commandservices;

import com.tp1202510030.backend.growrooms.domain.model.commands.measurement.AddMeasurementsToCurrentPhaseCommand;
import com.tp1202510030.backend.growrooms.domain.model.commands.measurement.CreateMeasurementCommand;
import com.tp1202510030.backend.growrooms.domain.model.entities.Measurement;
import com.tp1202510030.backend.growrooms.domain.services.measurement.MeasurementCommandService;
import com.tp1202510030.backend.growrooms.infrastructure.persistence.jpa.repositories.CropPhaseRepository;
import com.tp1202510030.backend.growrooms.infrastructure.persistence.jpa.repositories.CropRepository;
import com.tp1202510030.backend.growrooms.infrastructure.persistence.jpa.repositories.MeasurementRepository;
import org.springframework.stereotype.Service;

@Service
public class MeasurementCommandServiceImpl implements MeasurementCommandService {
    private final MeasurementRepository measurementRepository;
    private final CropPhaseRepository cropPhaseRepository;
    private final CropRepository cropRepository;

    public MeasurementCommandServiceImpl(MeasurementRepository measurementRepository, CropPhaseRepository cropPhaseRepository, CropRepository cropRepository) {
        this.measurementRepository = measurementRepository;
        this.cropPhaseRepository = cropPhaseRepository;
        this.cropRepository = cropRepository;
    }

    @Override
    public Long handle(CreateMeasurementCommand command) {
        var cropPhaseOpt = cropPhaseRepository.findById(command.cropPhaseId());
        if (cropPhaseOpt.isEmpty())
            throw new IllegalArgumentException("Crop phase with id %s not found".formatted(command.cropPhaseId()));

        var cropPhase = cropPhaseOpt.get();

        var measurement = new Measurement(
                command.parameter(),
                command.value(),
                command.timestamp(),
                cropPhase
        );
        measurementRepository.save(measurement);
        return measurement.getId();
    }

    @Override
    public void handle(AddMeasurementsToCurrentPhaseCommand command) {
        var cropOpt = cropRepository.findById(command.cropId());
        if (cropOpt.isEmpty()) {
            throw new IllegalArgumentException("Crop not found with id " + command.cropId());
        }
        var crop = cropOpt.get();

        var currentPhase = crop.getCurrentPhase();
        if (currentPhase == null) {
            throw new IllegalStateException("Crop does not have a current phase set");
        }

        var measurements = command.measurements().stream()
                .map(m -> new Measurement(m.parameter(), m.value(), m.timestamp(), currentPhase))
                .toList();

        measurementRepository.saveAll(measurements);
    }
}
