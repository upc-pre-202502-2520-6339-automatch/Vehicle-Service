package com.vehicle.domain.model.commands;

public record ChangeVehicleMainImageCommand(
        Long vehicleId,
        String mainImageUrl // puede venir null para quitar la imagen
) {}
