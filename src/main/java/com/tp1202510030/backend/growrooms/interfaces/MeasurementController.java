package com.tp1202510030.backend.growrooms.interfaces;

import com.tp1202510030.backend.growrooms.domain.model.commands.measurement.AddMeasurementsToCurrentPhaseCommand;
import com.tp1202510030.backend.growrooms.domain.services.measurement.MeasurementCommandService;
import com.tp1202510030.backend.growrooms.domain.services.measurement.MeasurementQueryService;
import com.tp1202510030.backend.growrooms.domain.model.queries.measurement.GetMeasurementByIdQuery;
import com.tp1202510030.backend.growrooms.interfaces.rest.resources.measurement.CreateMeasurementResource;
import com.tp1202510030.backend.growrooms.interfaces.rest.resources.measurement.AddMeasurementsToCurrentPhaseResource;
import com.tp1202510030.backend.growrooms.interfaces.rest.resources.measurement.MeasurementResource;
import com.tp1202510030.backend.growrooms.interfaces.rest.transform.measurement.AddMeasurementsToCurrentPhaseCommandFromResourceAssembler;
import com.tp1202510030.backend.growrooms.interfaces.rest.transform.measurement.CreateMeasurementCommandFromResourceAssembler;
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
     * Create a single measurement for a given crop phase
     *
     * @param createMeasurementResource The measurement details
     * @return The {@link MeasurementResource} for the created measurement
     */
    @PostMapping
    @Operation(
            summary = "Create a single measurement",
            description = "Creates a new measurement for the provided crop phase and returns the created measurement resource.",
            tags = {"Measurements"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Measurement created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MeasurementResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input or unable to create measurement",
                    content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<MeasurementResource> createMeasurement(@RequestBody CreateMeasurementResource createMeasurementResource) {
        var createMeasurementCommand = CreateMeasurementCommandFromResourceAssembler.toCommandFromResource(createMeasurementResource);
        var measurementId = measurementCommandService.handle(createMeasurementCommand);

        if (measurementId == 0L) {
            return ResponseEntity.badRequest().build();
        }

        var getMeasurementByIdQuery = new GetMeasurementByIdQuery(measurementId);
        var measurement = measurementQueryService.handle(getMeasurementByIdQuery);

        if (measurement.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        var measurementResource = MeasurementResourceFromEntityAssembler.toResourceFromEntity(measurement.get());
        return ResponseEntity.ok(measurementResource);
    }

    /**
     * Add multiple measurements to the current phase of a crop
     *
     * @param cropId                            The crop id
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
        var addMeasurementsCommand = AddMeasurementsToCurrentPhaseCommandFromResourceAssembler.toCommand(
                cropId,
                addMeasurementsToCurrentPhaseResource.measurements()
        );

        measurementCommandService.handle(addMeasurementsCommand);
        return ResponseEntity.ok().build();
    }

    /**
     * Get a measurement by its ID
     *
     * @param measurementId The measurement id
     * @return The {@link MeasurementResource} for the specified measurement
     */
    @GetMapping("/{measurementId}")
    @Operation(
            summary = "Get a measurement by its ID",
            description = "Retrieves the details of a specific measurement by its ID.",
            tags = {"Measurements"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Measurement found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MeasurementResource.class))),
            @ApiResponse(responseCode = "404", description = "Measurement not found",
                    content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<MeasurementResource> getMeasurementById(@PathVariable Long measurementId) {
        var query = new GetMeasurementByIdQuery(measurementId);
        var measurement = measurementQueryService.handle(query);

        if (measurement.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var measurementResource = MeasurementResourceFromEntityAssembler.toResourceFromEntity(measurement.get());
        return ResponseEntity.ok(measurementResource);
    }
}
