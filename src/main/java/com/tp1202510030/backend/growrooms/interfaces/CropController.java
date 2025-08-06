package com.tp1202510030.backend.growrooms.interfaces;

import com.tp1202510030.backend.growrooms.domain.model.aggregates.Crop;
import com.tp1202510030.backend.growrooms.domain.model.commands.crop.AdvanceCropPhaseCommand;
import com.tp1202510030.backend.growrooms.domain.model.commands.crop.FinishCropCommand;
import com.tp1202510030.backend.growrooms.domain.model.queries.crop.GetCropByIdQuery;
import com.tp1202510030.backend.growrooms.domain.model.queries.crop.GetCropsByGrowRoomIdQuery;
import com.tp1202510030.backend.growrooms.domain.model.queries.crop.GetFinishedCropsByGrowRoomIdQuery;
import com.tp1202510030.backend.growrooms.domain.services.crop.CropCommandService;
import com.tp1202510030.backend.growrooms.domain.services.crop.CropQueryService;
import com.tp1202510030.backend.growrooms.interfaces.rest.resources.crop.CreateCropResource;
import com.tp1202510030.backend.growrooms.interfaces.rest.resources.crop.CropResource;
import com.tp1202510030.backend.growrooms.interfaces.rest.resources.crop.FinishCropResource;
import com.tp1202510030.backend.growrooms.interfaces.rest.transform.crop.CreateCropCommandFromResourceAssembler;
import com.tp1202510030.backend.growrooms.interfaces.rest.transform.crop.CropResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1/crops", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Crops", description = "Crop Management Endpoints")
public class CropController {

    private final CropCommandService cropCommandService;
    private final CropQueryService cropQueryService;

    public CropController(CropCommandService cropCommandService, CropQueryService cropQueryService) {
        this.cropCommandService = cropCommandService;
        this.cropQueryService = cropQueryService;
    }

    /**
     * Create a new crop for a grow room
     *
     * @param createCropResource The resource containing crop details including sensor frequency and phases
     * @return The created CropResource with full crop details
     */
    @PostMapping
    @Operation(
            summary = "Create a new crop",
            description = "Creates a new crop associated to a grow room with sensor activation frequency and defined phases.",
            tags = {"Crops"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Crop created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CropResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input or unable to create crop",
                    content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<CropResource> createCrop(@RequestParam Long growRoomId, @RequestBody CreateCropResource createCropResource) {
        var createCropCommand = CreateCropCommandFromResourceAssembler.toCommandFromResource(createCropResource, growRoomId);
        var cropId = cropCommandService.handle(createCropCommand);

        if (cropId == 0L) {
            return ResponseEntity.badRequest().build();
        }

        var getCropByIdQuery = new GetCropByIdQuery(cropId);
        Optional<Crop> cropOpt = cropQueryService.handle(getCropByIdQuery);

        if (cropOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        var cropResource = CropResourceFromEntityAssembler.toResourceFromEntity(cropOpt.get());
        return ResponseEntity.ok(cropResource);
    }


    @PostMapping("/advancePhase/{cropId}")
    @Operation(
            summary = "Advance the crop phase",
            description = "Advances the current phase of the specified crop to the next phase.",
            tags = {"Crops"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Crop phase advanced successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input or unable to advance phase",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Crop not found or no phases available",
                    content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<Void> advanceCropPhase(@PathVariable Long cropId) {
        cropCommandService.handle(new AdvanceCropPhaseCommand(cropId));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{cropId}/finish")
    @Operation(
            summary = "Finish a crop and record total production",
            description = "Marks a crop as finished, sets its end date, and records the total production.",
            tags = {"Crops"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Crop finished successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input, e.g., crop not on last phase"),
            @ApiResponse(responseCode = "404", description = "Crop not found")
    })
    public ResponseEntity<Void> finishCrop(@PathVariable Long cropId, @RequestBody FinishCropResource resource) {
        var command = new FinishCropCommand(cropId, resource.totalProduction());
        if (resource.totalProduction() == null) {
            return ResponseEntity.badRequest().build();
        }

        cropCommandService.handle(command);
        return ResponseEntity.ok().build();
    }


    /**
     * Get crop by ID
     *
     * @param cropId The crop ID to retrieve
     * @return The CropResource associated with the given crop ID
     */
    @GetMapping("/{cropId}")
    @Operation(
            summary = "Get crop by ID",
            description = "Retrieves a crop by its ID.",
            tags = {"Crops"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Crop retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CropResource.class))),
            @ApiResponse(responseCode = "404", description = "Crop not found")
    })
    public ResponseEntity<CropResource> getCropById(@PathVariable Long cropId) {
        var query = new GetCropByIdQuery(cropId);
        var crop = cropQueryService.handle(query)
                .orElseThrow(() -> new IllegalArgumentException("Crop with ID " + cropId + " not found."));

        var cropResource = CropResourceFromEntityAssembler.toResourceFromEntity(crop);

        return ResponseEntity.ok(cropResource);
    }

    /**
     * Get crops by grow room ID
     *
     * @return List of CropResources associated to the grow room
     */
    @GetMapping
    @Operation(
            summary = "Get crops by grow room ID",
            description = "Retrieves a list of crops associated with a given grow room ID.",
            tags = {"Crops"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Crops retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CropResource.class))),
            @ApiResponse(responseCode = "204", description = "No crops found for the grow room")
    })
    public ResponseEntity<List<CropResource>> getCropsByGrowRoomId(@RequestParam Long growRoomId, @ParameterObject Pageable pageable) {
        var query = new GetCropsByGrowRoomIdQuery(growRoomId);
        Page<Crop> crops = cropQueryService.handle(query, pageable);

        if (crops.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        var resources = crops.stream()
                .map(CropResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(resources);
    }

    @GetMapping("/finished")
    @Operation(
            summary = "Get finished crops by grow room ID",
            description = "Retrieves a list of crops that have an end date (finished crops) by grow room ID.",
            tags = {"Crops"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Finished crops retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CropResource.class))),
            @ApiResponse(responseCode = "204", description = "No finished crops found for the requested grow room")
    })
    public ResponseEntity<List<CropResource>> getFinishedCrops(@RequestParam Long growRoomId, @ParameterObject Pageable pageable) {
        var query = new GetFinishedCropsByGrowRoomIdQuery(growRoomId);
        Page<Crop> crops = cropQueryService.handle(query, pageable);

        if (crops.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        var resources = crops.stream()
                .map(CropResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(resources);
    }
}