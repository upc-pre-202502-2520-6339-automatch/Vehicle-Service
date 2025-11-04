package com.vehicle.domain.model.events;


public record VehicleCreatedEvent(Long vehicleId, String plate, String vin) {}