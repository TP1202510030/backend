package com.tp1202510030.backend.growrooms.interfaces;

import com.tp1202510030.backend.growrooms.domain.model.entities.ControlAction;
import com.tp1202510030.backend.growrooms.domain.model.queries.controlaction.GetAllControlActionsByCropPhaseIdQuery;
import com.tp1202510030.backend.growrooms.domain.model.queries.controlaction.GetControlActionsForCurrentPhaseByCropIdQuery;
import com.tp1202510030.backend.growrooms.domain.services.controlaction.ControlActionCommandService;
import com.tp1202510030.backend.growrooms.domain.services.controlaction.ControlActionQueryService;
import com.tp1202510030.backend.growrooms.interfaces.rest.resources.controlaction.AddControlActionsToCurrentPhaseResource;
import com.tp1202510030.backend.growrooms.interfaces.rest.resources.controlaction.ControlActionResource;
import com.tp1202510030.backend.growrooms.interfaces.rest.transform.controlaction.AddControlActionsToCurrentPhaseCommandFromResourceAssembler;
import com.tp1202510030.backend.growrooms.interfaces.rest.transform.controlaction.ControlActionResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/control_actions", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Control Actions", description = "Control Actions Management Endpoints")
public class ControlActionController {
    private final ControlActionCommandService controlActionCommandService;
    private final ControlActionQueryService controlActionQueryService;

    public ControlActionController(ControlActionCommandService controlActionCommandService, ControlActionQueryService controlActionQueryService) {
        this.controlActionCommandService = controlActionCommandService;
        this.controlActionQueryService = controlActionQueryService;
    }

    /**
     * Add multiple control actions to the current phase of a crop
     *
     * @param cropId                                  The crop id
     * @param addControlActionsToCurrentPhaseResource The list of control actions to add to the current phase
     * @return A response indicating success or failure
     */
    @PostMapping("/addToCurrentPhase/{cropId}")
    @Operation(
            summary = "Add multiple control actions to the current phase of a crop",
            description = "Adds multiple control actions to the current phase of the specified crop and returns a success message.",
            tags = {"Control Actions"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "ControlActions added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input or unable to add controlActions",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Crop not found or no current phase set",
                    content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<Void> addControlActionsToCurrentPhase(@PathVariable Long cropId,
                                                                @RequestBody AddControlActionsToCurrentPhaseResource addControlActionsToCurrentPhaseResource) {
        var addControlActionsCommand = AddControlActionsToCurrentPhaseCommandFromResourceAssembler.toCommandFromResourceAssembler(
                cropId,
                addControlActionsToCurrentPhaseResource.controlActions()
        );

        try {
            controlActionCommandService.handle(addControlActionsCommand);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get control actions by crop phase ID
     *
     * @param cropPhaseId The crop phase ID to filter crops
     * @return List of ControlActionResources associated to the crop phase
     */
    @GetMapping
    @Operation(
            summary = "Get control actions by crop phase ID",
            description = "Retrieves a paginated list of controlActions associated with a given crop phase ID.",
            tags = {"Control Actions"}
    )
    public ResponseEntity<Page<ControlActionResource>> getControlActionsByCropPhaseId(@RequestParam Long cropPhaseId, @ParameterObject Pageable pageable) {
        var query = new GetAllControlActionsByCropPhaseIdQuery(cropPhaseId);
        Page<ControlAction> controlActionsPage = controlActionQueryService.handle(query, pageable);

        if (controlActionsPage.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        var resources = controlActionsPage.map(ControlActionResourceFromEntityAssembler::toResourceFromEntity);

        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{cropId}")
    @Operation(
            summary = "Get control Actions for the current phase of a crop",
            description = "Retrieve all controlActions for the current phase of a crop by its ID.",
            tags = {"Control Actions"}
    )
    public ResponseEntity<Page<ControlActionResource>> getControlActionsForCurrentPhase(
            @PathVariable Long cropId, @ParameterObject Pageable pageable) {
        var query = new GetControlActionsForCurrentPhaseByCropIdQuery(cropId);
        Page<ControlAction> controlActionsPage = controlActionQueryService.handle(query, pageable);

        if (controlActionsPage.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        var resources = controlActionsPage.map(ControlActionResourceFromEntityAssembler::toResourceFromEntity);

        return ResponseEntity.ok(resources);
    }
}