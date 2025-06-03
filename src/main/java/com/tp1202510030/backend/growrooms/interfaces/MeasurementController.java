package com.tp1202510030.backend.growrooms.interfaces;

import com.tp1202510030.backend.growrooms.domain.services.measurement.MeasurementCommandService;
import com.tp1202510030.backend.growrooms.domain.services.measurement.MeasurementQueryService;
import com.tp1202510030.backend.growrooms.interfaces.rest.resources.measurement.AddMeasurementsToCurrentPhaseResource;
import com.tp1202510030.backend.growrooms.interfaces.rest.transform.measurement.AddMeasurementsToCurrentPhaseCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
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
}
