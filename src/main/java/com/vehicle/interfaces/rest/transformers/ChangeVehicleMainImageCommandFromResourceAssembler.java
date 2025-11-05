package com.vehicle.interfaces.rest.transformers;

import com.vehicle.domain.model.commands.ChangeVehicleMainImageCommand;
import com.vehicle.interfaces.rest.resources.UpdateMainImageResource;

public class ChangeVehicleMainImageCommandFromResourceAssembler {
    public static ChangeVehicleMainImageCommand toCommand(Long id, UpdateMainImageResource r) {
        return new ChangeVehicleMainImageCommand(id, r.mainImageUrl());
    }
}


