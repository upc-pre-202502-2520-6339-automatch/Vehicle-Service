package com.vehicle.interfaces.rest.transformers;


import com.vehicle.domain.model.commands.ChangeVehicleStatusCommand;
import com.vehicle.interfaces.rest.resources.ChangeVehicleStatusResource;

public class ChangeVehicleStatusCommandFromResourceAssembler {
    public static ChangeVehicleStatusCommand toCommandFromResource(Long id, ChangeVehicleStatusResource r) {
        return new ChangeVehicleStatusCommand(id, r.newStatus());
    }
}
