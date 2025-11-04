package com.vehicle.infrastructure.messaging;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import com.vehicle.domain.model.events.VehicleCreatedEvent;
import com.vehicle.domain.model.events.VehicleUpdatedEvent;

@Service
@ConditionalOnProperty(name = "automatch.events.enabled", havingValue = "false")
public class NoopVehicleEvents implements VehicleEvents {
    @Override public void publishCreated(VehicleCreatedEvent e) { /* no-op */ }
    @Override public void publishUpdated(VehicleUpdatedEvent e) { /* no-op */ }
}