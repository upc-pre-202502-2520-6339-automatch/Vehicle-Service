package com.vehicle.interfaces.rest.resources;

import java.math.BigDecimal;

public record CreateVehicleResource(
        String plate,
        String vin,
        String brand,
        String model,
        Integer year,
        Integer mileageKm,
        BigDecimal priceAmount,
        String priceCurrency
) {}