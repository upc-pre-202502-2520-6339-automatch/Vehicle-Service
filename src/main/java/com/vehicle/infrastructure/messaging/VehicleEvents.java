package com.vehicle.infrastructure.messaging;

import com.vehicle.domain.model.events.VehicleCreatedEvent;
import com.vehicle.domain.model.events.VehicleUpdatedEvent;

public interface VehicleEvents {
    void publishCreated(VehicleCreatedEvent event);
    void publishUpdated(VehicleUpdatedEvent event);
}