package com.vehicle.domain.model.commands;


import com.vehicle.domain.model.valueobjects.VehicleStatus;

public record ChangeVehicleStatusCommand(
        Long vehicleId,
        VehicleStatus newStatus
) {}