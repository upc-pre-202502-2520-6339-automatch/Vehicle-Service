package com.vehicle.infrastructure.messaging;


import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import com.vehicle.domain.model.events.VehicleCreatedEvent;
import com.vehicle.domain.model.events.VehicleUpdatedEvent;

@Component
public class VehicleEventProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    public VehicleEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    public void publishCreated(VehicleCreatedEvent event){
        kafkaTemplate.send(KafkaTopicNames.VEHICLE_EVENTS, event.vehicleId().toString(), event);
    }
    public void publishUpdated(VehicleUpdatedEvent event){
        kafkaTemplate.send(KafkaTopicNames.VEHICLE_EVENTS, event.vehicleId().toString(), event);
    }
}


