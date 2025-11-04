package com.vehicle.interfaces.rest.resources;



import java.math.BigDecimal;

public record UpdateVehicleResource(
        String brand,
        String model,
        Integer year,
        Integer mileageKm,
        BigDecimal priceAmount,
        String priceCurrency
) {}