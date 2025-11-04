package com.vehicle.interfaces.rest.resources;

import com.vehicle.domain.model.valueobjects.VehicleStatus;

public record ChangeVehicleStatusResource(
        VehicleStatus newStatus
) {}