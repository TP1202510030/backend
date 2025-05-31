package com.tp1202510030.backend.growrooms.interfaces;

import com.tp1202510030.backend.growrooms.domain.model.queries.growroom.GetGrowRoomsByCompanyIdQuery;
import com.tp1202510030.backend.growrooms.interfaces.rest.resources.growroom.CreateGrowRoomResource;
import com.tp1202510030.backend.growrooms.interfaces.rest.resources.growroom.GrowRoomResource;
import com.tp1202510030.backend.growrooms.domain.model.queries.growroom.GetGrowRoomByIdQuery;
import com.tp1202510030.backend.growrooms.domain.services.growroom.GrowRoomCommandService;
import com.tp1202510030.backend.growrooms.domain.services.growroom.GrowRoomQueryService;
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
     * @param createGrowRoomResource The Grow Room to be created
     * @return The { @link growRoomResource} resource for the created growRoom
     */
    @PostMapping
    @Operation(
            summary = "Create a new Grow Room",
            description = "Creates a new Grow Room with the provided details and returns the created grow room resource.",
            tags = {"Companies"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Grow Room created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GrowRoomResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input or unable to create Grow Room",
                    content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<GrowRoomResource> createGrowRoom(@RequestBody CreateGrowRoomResource createGrowRoomResource) {
        var createGrowRoomCommand = CreateGrowRoomCommandFromResourceAssembler.toCommandFromResource(createGrowRoomResource);
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
     * @param growRoomId The growRoom id
     * @param resource    The {@link UpdateGrowRoomResource} instance
     * @return The {@link GrowRoomResource} resource for the updated growRoom
     */
    @PutMapping("/{growRoomId}")
    @Operation(summary = "Update Grow Room", description = "Update Grow Room")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Grow Room updated"),
            @ApiResponse(responseCode = "404", description = "Grow Room not found")})
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

    @GetMapping
    @Operation(
            summary = "Get grow rooms by company ID",
            description = "Retrieves a list of grow rooms by the provided company ID.",
            tags = {"Companies"}
    )
    public ResponseEntity<List<GrowRoomResource>> getGrowRoomsByCompanyId(@RequestParam Long companyId) {
        var query = new GetGrowRoomsByCompanyIdQuery(companyId);
        var growRooms = growRoomQueryService.handle(query);

        if (growRooms.isEmpty()) {
            return ResponseEntity.noContent().build();  // O ResponseEntity.ok(Collections.emptyList());
        }

        var resources = growRooms.stream()
                .map(GrowRoomResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(resources);
    }

}
