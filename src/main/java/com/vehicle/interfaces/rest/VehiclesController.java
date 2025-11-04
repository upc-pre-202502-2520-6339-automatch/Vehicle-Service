package com.vehicle.interfaces.rest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
public class VehiclesController {

    private final VehicleCommandServiceImpl commandService;
    private final VehicleQueryServiceImpl queryService;

    public VehiclesController(VehicleCommandServiceImpl commandService,
                              VehicleQueryServiceImpl queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @PostMapping
    public ResponseEntity<VehicleResource> create(@RequestBody CreateVehicleResource resource) {
        var id = commandService.handle(
                CreateVehicleCommandFromResourceAssembler.toCommandFromResource(resource)
        );
        var vehicle = queryService.handle(new GetVehicleByIdQuery(id));
        return ResponseEntity.ok(VehicleResourceFromEntityAssembler.toResourceFromEntity(vehicle));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<VehicleResource> update(@PathVariable Long id,
                                                  @RequestBody UpdateVehicleResource resource) {
        commandService.handle(UpdateVehicleCommandFromResourceAssembler.toCommandFromResource(id, resource));
        var vehicle = queryService.handle(new GetVehicleByIdQuery(id));
        return ResponseEntity.ok(VehicleResourceFromEntityAssembler.toResourceFromEntity(vehicle));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<VehicleResource> changeStatus(@PathVariable Long id,
                                                        @RequestBody ChangeVehicleStatusResource resource) {
        commandService.handle(ChangeVehicleStatusCommandFromResourceAssembler.toCommandFromResource(id, resource));
        var vehicle = queryService.handle(new GetVehicleByIdQuery(id));
        return ResponseEntity.ok(VehicleResourceFromEntityAssembler.toResourceFromEntity(vehicle));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleResource> getById(@PathVariable Long id) {
        var vehicle = queryService.handle(new GetVehicleByIdQuery(id));
        return ResponseEntity.ok(VehicleResourceFromEntityAssembler.toResourceFromEntity(vehicle));
    }

    // 1) Versión paginada (la que ya tenías)
    @GetMapping
    public ResponseEntity<Page<VehicleResource>> search(
            @RequestParam(value = "status", required = false) VehicleStatus status,
            Pageable pageable) {

        var page = queryService.handle(new GetVehiclesByFiltersQuery(status), pageable)
                .map(VehicleResourceFromEntityAssembler::toResourceFromEntity);

        return ResponseEntity.ok(page);
    }

    // 2) Versión SIN paginación (para Angular)
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
}
