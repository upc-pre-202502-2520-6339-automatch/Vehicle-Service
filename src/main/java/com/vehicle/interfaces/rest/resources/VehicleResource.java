package com.vehicle.interfaces.rest.resources;


import com.vehicle.domain.model.valueobjects.VehicleStatus;

import java.math.BigDecimal;

public record VehicleResource(
        Long id,
        String plate,
        String vin,
        String brand,
        String model,
        Integer year,
        Integer mileageKm,
        BigDecimal priceAmount,
        String priceCurrency,
        VehicleStatus status,
        String mainImageUrl
) {}