package com.vehicle.domain.model.commands;

import java.math.BigDecimal;

public record UpdateVehicleDetailsCommand(
        Long vehicleId,
        String brand,
        String model,
        Integer year,
        Integer mileageKm,
        BigDecimal priceAmount,
        String priceCurrency
) {}