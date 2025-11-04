package com.vehicle.domain.model.queries;

import com.vehicle.domain.model.valueobjects.VehicleStatus;

public record GetVehiclesByFiltersQuery(VehicleStatus status) {}