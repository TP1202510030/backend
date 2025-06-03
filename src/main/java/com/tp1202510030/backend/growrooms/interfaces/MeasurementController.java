package com.tp1202510030.backend.growrooms.interfaces;

import com.tp1202510030.backend.growrooms.domain.model.entities.Measurement;
import com.tp1202510030.backend.growrooms.domain.model.queries.measurement.GetAllMeasurementsByCropPhaseId;
import com.tp1202510030.backend.growrooms.domain.services.measurement.MeasurementCommandService;
import com.tp1202510030.backend.growrooms.domain.services.measurement.MeasurementQueryService;
import com.tp1202510030.backend.growrooms.interfaces.rest.resources.crop.CropResource;
import com.tp1202510030.backend.growrooms.interfaces.rest.resources.measurement.AddMeasurementsToCurrentPhaseResource;
import com.tp1202510030.backend.growrooms.interfaces.rest.resources.measurement.MeasurementResource;
import com.tp1202510030.backend.growrooms.interfaces.rest.transform.measurement.AddMeasurementsToCurrentPhaseCommandFromResourceAssembler;
import com.tp1202510030.backend.growrooms.interfaces.rest.transform.measurement.MeasurementResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/measurements", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Measurements", description = "Measurements Management Endpoints")
public class MeasurementController {
    private final MeasurementCommandService measurementCommandService;
    private final MeasurementQueryService measurementQueryService;

    public MeasurementController(MeasurementCommandService measurementCommandService, MeasurementQueryService measurementQueryService) {
        this.measurementCommandService = measurementCommandService;
        this.measurementQueryService = measurementQueryService;
    }

    /**
     * Add multiple measurements to the current phase of a crop
     *
     * @param cropId                                The crop id
     * @param addMeasurementsToCurrentPhaseResource The list of measurements to add to the current phase
     * @return A response indicating success or failure
     */
    @PostMapping("/addToCurrentPhase/{cropId}")
    @Operation(
            summary = "Add multiple measurements to the current phase of a crop",
            description = "Adds multiple measurements to the current phase of the specified crop and returns a success message.",
            tags = {"Measurements"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Measurements added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input or unable to add measurements",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Crop not found or no current phase set",
                    content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<Void> addMeasurementsToCurrentPhase(@PathVariable Long cropId,
                                                              @RequestBody AddMeasurementsToCurrentPhaseResource addMeasurementsToCurrentPhaseResource) {
        var addMeasurementsCommand = AddMeasurementsToCurrentPhaseCommandFromResourceAssembler.toCommandFromResourceAssembler(
                cropId,
                addMeasurementsToCurrentPhaseResource.measurements()
        );

        try {
            measurementCommandService.handle(addMeasurementsCommand);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get measurements by crop phase ID
     *
     * @param cropPhaseId The crop phase ID to filter crops
     * @return List of MeasurementResources associated to the crop phase
     */
    @GetMapping
    @Operation(
            summary = "Get measurements by crop phase ID",
            description = "Retrieves a list of measurements associated with a given crop phase ID.",
            tags = {"Measurements"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Measurements retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CropResource.class))),
            @ApiResponse(responseCode = "204", description = "No measurements found for the grow room")
    })
    public ResponseEntity<List<MeasurementResource>> getCropsByGrowRoomId(@RequestParam Long cropPhaseId) {
        var query = new GetAllMeasurementsByCropPhaseId(cropPhaseId);
        List<Measurement> measurements = measurementQueryService.handle(query);

        if (measurements.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        var resources = measurements.stream()
                .map(MeasurementResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(resources);
    }
}
