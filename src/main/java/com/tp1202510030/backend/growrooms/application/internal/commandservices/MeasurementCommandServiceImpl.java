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
        var currentPhase = cropRepository.findActivePhaseByCropIdOrThrow(command.cropId());

        var measurements = command.measurements().stream()
                .map(m -> {
                    String unit = m.parameter().getUnit();
                    return new Measurement(m.parameter(), m.value(), unit, currentPhase);
                })
                .toList();

        try {
            measurementRepository.saveAll(measurements);
        } catch (Exception e) {
            throw new RuntimeException("Error while saving measurements: " + e.getMessage(), e);
        }
    }

}
