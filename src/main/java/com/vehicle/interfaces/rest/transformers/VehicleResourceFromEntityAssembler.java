package com.vehicle.interfaces.rest.transformers;
import com.vehicle.domain.model.aggregates.Vehicle;
import com.vehicle.interfaces.rest.resources.VehicleResource;

public class VehicleResourceFromEntityAssembler {
    public static VehicleResource toResourceFromEntity(Vehicle v) {
        return new VehicleResource(
                v.getId(),
                v.getPlate().getValue(),
                v.getVin().getValue(),
                v.getBrand(),
                v.getModel(),
                v.getYear().getYear(),            // <- antes getValue()
                v.getMileage().getKilometers(),   // <- antes getValue()
                v.getPrice().getAmount(),
                v.getPrice().getCurrency(),
                v.getStatus()
        );
    }
}