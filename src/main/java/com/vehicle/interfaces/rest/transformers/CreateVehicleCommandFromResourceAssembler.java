package com.vehicle.interfaces.rest.transformers;

import com.vehicle.domain.model.commands.CreateVehicleCommand;
import com.vehicle.interfaces.rest.resources.CreateVehicleResource;

public class CreateVehicleCommandFromResourceAssembler {
    public static CreateVehicleCommand toCommandFromResource(CreateVehicleResource r) {
        return new CreateVehicleCommand(
                r.plate(), r.vin(), r.brand(), r.model(),
                r.year(), r.mileageKm(), r.priceAmount(), r.priceCurrency(),
                r.mainImageUrl()
        );
    }
}

