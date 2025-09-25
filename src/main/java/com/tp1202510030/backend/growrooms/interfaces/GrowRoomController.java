package com.tp1202510030.backend.growrooms.interfaces;

import com.tp1202510030.backend.growrooms.domain.model.commands.growroom.DeleteGrowRoomCommand;
import com.tp1202510030.backend.growrooms.domain.model.queries.growroom.GetGrowRoomByIdQuery;
import com.tp1202510030.backend.growrooms.domain.model.queries.growroom.GetGrowRoomsByCompanyIdQuery;
import com.tp1202510030.backend.growrooms.domain.services.growroom.GrowRoomCommandService;
import com.tp1202510030.backend.growrooms.domain.services.growroom.GrowRoomQueryService;
import com.tp1202510030.backend.growrooms.interfaces.rest.resources.growroom.CreateGrowRoomResource;
import com.tp1202510030.backend.growrooms.interfaces.rest.resources.growroom.DeviceCredentialsResource;
import com.tp1202510030.backend.growrooms.interfaces.rest.resources.growroom.GrowRoomResource;
import com.tp1202510030.backend.growrooms.interfaces.rest.resources.growroom.PatchGrowRoomResource;
import com.tp1202510030.backend.growrooms.interfaces.rest.transform.growroom.CreateGrowRoomCommandFromResourceAssembler;
import com.tp1202510030.backend.growrooms.interfaces.rest.transform.growroom.DeviceCredentialsResourceFromDomainAssembler;
import com.tp1202510030.backend.growrooms.interfaces.rest.transform.growroom.GrowRoomResourceFromEntityAssembler;
import com.tp1202510030.backend.growrooms.interfaces.rest.transform.growroom.PatchGrowRoomCommandFromResourceAssembler;
import com.tp1202510030.backend.shared.domain.exceptions.ResourceNotFoundException;
import com.tp1202510030.backend.shared.infrastructure.authorization.SecurityConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Grow Rooms", description = "Grow Rooms Management Endpoints")
@SecurityRequirement(name = "bearerAuth")
public class GrowRoomController {
    private final GrowRoomQueryService growRoomQueryService;
    private final GrowRoomCommandService growRoomCommandService;

    public GrowRoomController(GrowRoomQueryService growRoomQueryService, GrowRoomCommandService growRoomCommandService) {
        this.growRoomQueryService = growRoomQueryService;
        this.growRoomCommandService = growRoomCommandService;
    }

    /**
     * Create a grow room
     *
     * @param createGrowRoomResource The grow room to be created
     * @return The { @link growRoomResource} resource for the created grow room
     */
    @PostMapping("/grow-rooms")
    @Operation(
            summary = "Create a new grow room and provision its IoT device",
            description = "Creates a new grow room and returns the credentials for its IoT device. These credentials should be downloaded immediately as they are not stored."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Grow room created and device provisioned successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DeviceCredentialsResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input or failed to provision device")
    })
    @PreAuthorize(SecurityConstants.IS_ADMIN)
    public ResponseEntity<DeviceCredentialsResource> createGrowRoom(
            @RequestParam Long companyId,
            @RequestBody CreateGrowRoomResource createGrowRoomResource
    ) {
        var createGrowRoomCommand = CreateGrowRoomCommandFromResourceAssembler.toCommandFromResource(createGrowRoomResource, companyId);

        var credentialsOptional = growRoomCommandService.handle(createGrowRoomCommand);

        if (credentialsOptional.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        var credentialsResource = DeviceCredentialsResourceFromDomainAssembler.toResourceFromDomain(credentialsOptional.get());

        return new ResponseEntity<>(credentialsResource, HttpStatus.CREATED);
    }

    /*
     * Update Grow Room
     *
     * @param growRoomId The grow room id
     * @param resource   The {@link UpdateGrowRoomResource} instance
     * @return The {@link GrowRoomResource} resource for the updated growRoom

    @PutMapping("/grow-rooms/{growRoomId}")
    @Operation(
            summary = "Update grow room",
            description = "Update grow room",
            tags = {"Grow Rooms"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Grow room updated"),
            @ApiResponse(responseCode = "404", description = "Grow room not found")})
    @PreAuthorize(SecurityConstants.ADMIN_OR_GROW_ROOM_OWNER)
    public ResponseEntity<GrowRoomResource> updateGrowRoom(@PathVariable Long growRoomId, @RequestBody UpdateGrowRoomResource resource) {
        var updateGrowRoomCommand = UpdateGrowRoomCommandFromResourceAssembler.toCommandFromResource(growRoomId, resource);
        var updatedGrowRoom = growRoomCommandService.handle(updateGrowRoomCommand);

        if (updatedGrowRoom.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var updatedGrowRoomEntity = updatedGrowRoom.get();
        var updatedGrowRoomResource = GrowRoomResourceFromEntityAssembler.toResourceFromEntity(updatedGrowRoomEntity);
        return ResponseEntity.ok(updatedGrowRoomResource);
    }
     */

    @PatchMapping("/grow-rooms/{growRoomId}")
    @Operation(
            summary = "Patch a grow room",
            description = "Updates one or more properties of a grow room. Only fields provided in the request body will be updated. This action is only available to administrators."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Grow room updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "403", description = "Forbidden. User is not an admin."),
            @ApiResponse(responseCode = "404", description = "Grow room not found")
    })
    @PreAuthorize(SecurityConstants.IS_ADMIN)
    public ResponseEntity<GrowRoomResource> patchGrowRoom(
            @PathVariable Long growRoomId,
            @RequestBody PatchGrowRoomResource resource
    ) {
        var command = PatchGrowRoomCommandFromResourceAssembler.toCommandFromResource(growRoomId, resource);
        var patchedGrowRoom = growRoomCommandService.handle(command);

        if (patchedGrowRoom.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var growRoomResource = GrowRoomResourceFromEntityAssembler.toResourceFromEntity(patchedGrowRoom.get());
        return ResponseEntity.ok(growRoomResource);
    }

    @GetMapping("/companies/{companyId}/grow-rooms")
    @Operation(
            summary = "Get grow rooms by company ID",
            description = "Retrieves a list of grow rooms by the provided company ID.",
            tags = {"Grow Rooms"}
    )
    @PreAuthorize(SecurityConstants.ADMIN_OR_COMPANY_OWNER)
    public ResponseEntity<Page<GrowRoomResource>> getGrowRoomsByCompanyId(@PathVariable Long companyId, @ParameterObject Pageable pageable) {
        var query = new GetGrowRoomsByCompanyIdQuery(companyId);
        var growRooms = growRoomQueryService.handle(query, pageable);

        if (growRooms.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        var resources = growRooms
                .map(GrowRoomResourceFromEntityAssembler::toResourceFromEntity);

        return ResponseEntity.ok(resources);
    }

    @GetMapping("/grow-rooms/{growRoomId}")
    @Operation(
            summary = "Get a grow room by ID",
            description = "Retrieves a grow room by its ID.",
            tags = {"Grow Rooms"}
    )
    @PreAuthorize(SecurityConstants.ADMIN_OR_GROW_ROOM_OWNER)
    public ResponseEntity<GrowRoomResource> getGrowRoomById(@PathVariable Long growRoomId) {
        var query = new GetGrowRoomByIdQuery(growRoomId);
        var growRoom = growRoomQueryService.handle(query)
                .orElseThrow(() -> new ResourceNotFoundException("GrowRoom", "ID", growRoomId.toString()));

        var resource = GrowRoomResourceFromEntityAssembler.toResourceFromEntity(growRoom);
        return ResponseEntity.ok(resource);
    }

    @DeleteMapping("/grow-rooms/{growRoomId}")
    @Operation(
            summary = "Delete a grow room",
            description = "Marks a grow room as deleted (soft delete). This action is only available to administrators."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Grow room deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden. User is not an admin."),
            @ApiResponse(responseCode = "404", description = "Grow room not found")
    })
    @PreAuthorize(SecurityConstants.IS_ADMIN)
    public ResponseEntity<Void> deleteGrowRoom(@PathVariable Long growRoomId) {
        var command = new DeleteGrowRoomCommand(growRoomId);
        growRoomCommandService.handle(command);
        return ResponseEntity.noContent().build();
    }
}
