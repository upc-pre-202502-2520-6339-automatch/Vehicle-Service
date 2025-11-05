package com.vehicle.interfaces.rest;

import com.vehicle.infrastructure.persistence.jpa.VehicleRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.vehicle.application.internal.commandservices.VehicleCommandServiceImpl;
import com.vehicle.application.internal.queryservices.VehicleQueryServiceImpl;
import com.vehicle.domain.model.queries.GetVehicleByIdQuery;
import com.vehicle.domain.model.queries.GetVehiclesByFiltersQuery;
import com.vehicle.domain.model.valueobjects.VehicleStatus;
import com.vehicle.interfaces.rest.resources.*;
import com.vehicle.interfaces.rest.transformers.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vehicles")
@Tag(name = "vehicles", description = "Vehicle CRUD and queries")
public class VehiclesController {

    private final VehicleCommandServiceImpl commandService;
    private final VehicleQueryServiceImpl queryService;
    private final VehicleRepository vehicleRepository;

    public VehiclesController(VehicleCommandServiceImpl commandService,
                              VehicleQueryServiceImpl queryService, VehicleRepository vehicleRepository) {
        this.commandService = commandService;
        this.queryService = queryService;
        this.vehicleRepository = vehicleRepository;
    }

    @Operation(summary = "Create vehicle")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vehicle created"),
            @ApiResponse(responseCode = "400", description = "Invalid payload"),
            @ApiResponse(responseCode = "409", description = "Plate already registered")
    })
    @PostMapping
    public ResponseEntity<VehicleResource> create(@RequestBody @Valid CreateVehicleResource resource) {
        var id = commandService.handle(
                CreateVehicleCommandFromResourceAssembler.toCommandFromResource(resource)
        );
        var vehicle = queryService.handle(new GetVehicleByIdQuery(id));
        return ResponseEntity.ok(VehicleResourceFromEntityAssembler.toResourceFromEntity(vehicle));
    }


    @Operation(summary = "Update vehicle details")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vehicle updated"),
            @ApiResponse(responseCode = "404", description = "Vehicle not found"),
            @ApiResponse(responseCode = "400", description = "Invalid payload")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<VehicleResource> update(@PathVariable Long id,
                                                  @RequestBody @Valid UpdateVehicleResource resource) {
        commandService.handle(UpdateVehicleCommandFromResourceAssembler.toCommandFromResource(id, resource));
        var vehicle = queryService.handle(new GetVehicleByIdQuery(id));
        return ResponseEntity.ok(VehicleResourceFromEntityAssembler.toResourceFromEntity(vehicle));
    }



    @Operation(summary = "Change vehicle status")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status changed"),
            @ApiResponse(responseCode = "404", description = "Vehicle not found"),
            @ApiResponse(responseCode = "400", description = "Invalid status")
    })
    @PatchMapping("/{id}/status")
    public ResponseEntity<VehicleResource> changeStatus(@PathVariable Long id,
                                                        @RequestBody ChangeVehicleStatusResource resource) {
        commandService.handle(ChangeVehicleStatusCommandFromResourceAssembler.toCommandFromResource(id, resource));
        var vehicle = queryService.handle(new GetVehicleByIdQuery(id));
        return ResponseEntity.ok(VehicleResourceFromEntityAssembler.toResourceFromEntity(vehicle));
    }


    @Operation(summary = "Get vehicle by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vehicle returned"),
            @ApiResponse(responseCode = "404", description = "Vehicle not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<VehicleResource> getById(@PathVariable Long id) {
        var vehicle = queryService.handle(new GetVehicleByIdQuery(id));
        return ResponseEntity.ok(VehicleResourceFromEntityAssembler.toResourceFromEntity(vehicle));
    }



    // 1) Versión paginada (la que ya tenías)
    @Operation(summary = "Search vehicles (paginated)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Page of vehicles returned")
    })
    @GetMapping
    public ResponseEntity<Page<VehicleResource>> search(
            @RequestParam(value="status", required=false) VehicleStatus status,
            Pageable pageable) {

        // Si no viene sort -> aplicamos createdAt desc usando PageRequest
        if (pageable.getSort().isUnsorted()) {
            pageable = PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by("createdAt").descending()
            );
        }

        var page = queryService.handle(new GetVehiclesByFiltersQuery(status), pageable)
                .map(VehicleResourceFromEntityAssembler::toResourceFromEntity);

        return ResponseEntity.ok(page);
    }




    // 2) Versión SIN paginación (para Angular)
    @Operation(summary = "List vehicles (unpaged)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of vehicles returned")
    })
    @GetMapping("/list")
    public ResponseEntity<List<VehicleResource>> findAll(
            @RequestParam(value = "status", required = false) VehicleStatus status) {

        // pedimos sin paginar
        var page = queryService.handle(new GetVehiclesByFiltersQuery(status), Pageable.unpaged());

        var list = page
                .map(VehicleResourceFromEntityAssembler::toResourceFromEntity)
                .getContent();

        return ResponseEntity.ok(list);
    }


    @Operation(summary = "Check vehicle existence by plate or VIN")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Exists flag returned"),
            @ApiResponse(responseCode = "400", description = "Either 'plate' or 'vin' must be sent")
    })
    @GetMapping("/internal/exists")
    public ResponseEntity<Boolean> exists(
            @RequestParam(required = false) String plate,
            @RequestParam(required = false) String vin) {

        // valida entrada: al menos uno requerido
        boolean hasPlate = plate != null && !plate.isBlank();
        boolean hasVin   = vin   != null && !vin.isBlank();

        if (!hasPlate && !hasVin) {
            throw new IllegalArgumentException("either 'plate' or 'vin' is required");
        }

        boolean exists = (hasPlate && vehicleRepository.existsByPlate_Value(plate))
                || (hasVin   && vehicleRepository.existsByVin_Value(vin));

        return ResponseEntity.ok(exists);
    }


    @Operation(summary = "Update main image of a vehicle")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Main image updated"),
            @ApiResponse(responseCode = "404", description = "Vehicle not found"),
            @ApiResponse(responseCode = "400", description = "Invalid body or URL")
    })
    @PatchMapping("/{id}/main-image")
    public ResponseEntity<VehicleResource> updateMainImage(
            @PathVariable Long id,
            @RequestBody UpdateMainImageResource body) {

        // valida opcionalmente string vacío como null
        var url = (body != null && body.mainImageUrl() != null && body.mainImageUrl().isBlank())
                ? null : (body != null ? body.mainImageUrl() : null);

        commandService.handle(
                ChangeVehicleMainImageCommandFromResourceAssembler.toCommand(id, new UpdateMainImageResource(url))
        );

        var vehicle = queryService.handle(new GetVehicleByIdQuery(id));
        return ResponseEntity.ok(VehicleResourceFromEntityAssembler.toResourceFromEntity(vehicle));
    }





}
