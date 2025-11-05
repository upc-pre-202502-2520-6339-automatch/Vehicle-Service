package com.vehicle.interfaces.rest.transformers;

import com.vehicle.domain.model.commands.UpdateVehicleDetailsCommand;
import com.vehicle.interfaces.rest.resources.UpdateVehicleResource;

public class UpdateVehicleCommandFromResourceAssembler {
    public static UpdateVehicleDetailsCommand toCommandFromResource(Long id, UpdateVehicleResource r) {
        return new UpdateVehicleDetailsCommand(
                id, r.brand(), r.model(), r.year(), r.mileageKm(), r.priceAmount(), r.priceCurrency(),
                r.mainImageUrl()
        );
    }
}

