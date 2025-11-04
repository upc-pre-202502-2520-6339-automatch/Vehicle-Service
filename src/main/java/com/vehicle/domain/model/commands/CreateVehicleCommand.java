package com.vehicle.domain.model.commands;

import java.math.BigDecimal;

public record CreateVehicleCommand(
        String plate,
        String vin,
        String brand,
        String model,
        Integer year,
        Integer mileageKm,
        BigDecimal priceAmount,
        String priceCurrency
) {}