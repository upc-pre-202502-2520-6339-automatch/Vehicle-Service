package com.vehicle.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateVehicleResource(
        @NotBlank String plate,
        @NotBlank String vin,
        @NotBlank String brand,
        @NotBlank String model,
        @NotNull Integer year,
        @NotNull Integer mileageKm,
        @NotNull BigDecimal priceAmount,
        @NotBlank String priceCurrency
) {}