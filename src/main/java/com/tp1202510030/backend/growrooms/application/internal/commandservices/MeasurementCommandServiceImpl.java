package com.tp1202510030.backend.growrooms.application.internal.commandservices;

import com.tp1202510030.backend.growrooms.domain.model.commands.measurement.AddMeasurementsToCurrentPhaseCommand;
import com.tp1202510030.backend.growrooms.domain.model.entities.Measurement;
import com.tp1202510030.backend.growrooms.domain.services.measurement.MeasurementCommandService;
import com.tp1202510030.backend.growrooms.infrastructure.persistence.jpa.repositories.CropRepository;
import com.tp1202510030.backend.growrooms.infrastructure.persistence.jpa.repositories.MeasurementRepository;
import org.springframework.stereotype.Service;

@Service
public class MeasurementCommandServiceImpl implements MeasurementCommandService {
    private final MeasurementRepository measurementRepository;
    private final CropRepository cropRepository;

    public MeasurementCommandServiceImpl(MeasurementRepository measurementRepository, CropRepository cropRepository) {
        this.measurementRepository = measurementRepository;
        this.cropRepository = cropRepository;
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
                .map(m -> new Measurement(m.parameter(), m.value(), currentPhase))
                .toList();

        try {
            measurementRepository.saveAll(measurements);
        } catch (Exception e) {
            throw new RuntimeException("Error while saving measurements: " + e.getMessage(), e);
        }
    }

}
