package com.tp1202510030.backend.growrooms.interfaces;

import com.tp1202510030.backend.growrooms.domain.model.queries.growroom.GetGrowRoomByIdQuery;
import com.tp1202510030.backend.growrooms.domain.model.queries.growroom.GetGrowRoomsByCompanyIdQuery;
import com.tp1202510030.backend.growrooms.domain.services.growroom.GrowRoomCommandService;
import com.tp1202510030.backend.growrooms.domain.services.growroom.GrowRoomQueryService;
import com.tp1202510030.backend.growrooms.interfaces.rest.resources.growroom.CreateGrowRoomResource;
import com.tp1202510030.backend.growrooms.interfaces.rest.resources.growroom.GrowRoomResource;
import com.tp1202510030.backend.growrooms.interfaces.rest.resources.growroom.UpdateGrowRoomResource;
import com.tp1202510030.backend.growrooms.interfaces.rest.transform.growroom.CreateGrowRoomCommandFromResourceAssembler;
import com.tp1202510030.backend.growrooms.interfaces.rest.transform.growroom.GrowRoomResourceFromEntityAssembler;
import com.tp1202510030.backend.growrooms.interfaces.rest.transform.growroom.UpdateGrowRoomCommandFromResourceAssembler;
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
@RequestMapping(value = "/api/v1/grow_rooms", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Grow Rooms", description = "Grow Rooms Management Endpoints")
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
    @PostMapping
    @Operation(
            summary = "Create a new grow room",
            description = "Creates a new grow room with the provided details and returns the created grow room resource.",
            tags = {"Grow Rooms"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Grow room created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GrowRoomResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input or unable to create grow room",
                    content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<GrowRoomResource> createGrowRoom(@RequestParam Long companyId, @RequestBody CreateGrowRoomResource createGrowRoomResource) {
        var createGrowRoomCommand = CreateGrowRoomCommandFromResourceAssembler.toCommandFromResource(createGrowRoomResource, companyId);
        var growRoomId = growRoomCommandService.handle(createGrowRoomCommand);

        if (growRoomId == 0L) {
            return ResponseEntity.badRequest().build();
        }

        var getGrowRoomByIdQuery = new GetGrowRoomByIdQuery(growRoomId);
        var growRoom = growRoomQueryService.handle(getGrowRoomByIdQuery);

        if (growRoom.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        var growRoomResource = GrowRoomResourceFromEntityAssembler.toResourceFromEntity(growRoom.get());
        return ResponseEntity.ok(growRoomResource);
    }

    /**
     * Update Grow Room
     *
     * @param growRoomId The grow room id
     * @param resource   The {@link UpdateGrowRoomResource} instance
     * @return The {@link GrowRoomResource} resource for the updated growRoom
     */
    @PutMapping("/{growRoomId}")
    @Operation(
            summary = "Update grow room",
            description = "Update grow room",
            tags = {"Grow Rooms"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Grow room updated"),
            @ApiResponse(responseCode = "404", description = "Grow room not found")})
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

    @GetMapping("/{companyId}")
    @Operation(
            summary = "Get grow rooms by company ID",
            description = "Retrieves a list of grow rooms by the provided company ID.",
            tags = {"Grow Rooms"}
    )
    public ResponseEntity<List<GrowRoomResource>> getGrowRoomsByCompanyId(@PathVariable Long companyId) {
        var query = new GetGrowRoomsByCompanyIdQuery(companyId);
        var growRooms = growRoomQueryService.handle(query);

        if (growRooms.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        var resources = growRooms.stream()
                .map(GrowRoomResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(resources);
    }
}
