package com.vehicle.infrastructure.messaging;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.vehicle.domain.model.events.VehicleCreatedEvent;
import com.vehicle.domain.model.events.VehicleUpdatedEvent;

@Service
@ConditionalOnProperty(name = "automatch.events.enabled", havingValue = "true", matchIfMissing = true)
public class KafkaVehicleEvents implements VehicleEvents {

    private final KafkaTemplate<String, Object> kafka;
    private final String createdTopic;
    private final String updatedTopic;

    public KafkaVehicleEvents(
            KafkaTemplate<String, Object> kafka,
            @Value("${automatch.topics.vehicles.created:vehicles.created}") String createdTopic,
            @Value("${automatch.topics.vehicles.updated:vehicles.updated}") String updatedTopic) {
        this.kafka = kafka;
        this.createdTopic = createdTopic;
        this.updatedTopic = updatedTopic;
    }

    @Override public void publishCreated(VehicleCreatedEvent e) { send(createdTopic, e); }
    @Override public void publishUpdated(VehicleUpdatedEvent e) { send(updatedTopic, e); }

    private void send(String topic, Object payload) {
        // envío asíncrono: no bloquea ni hace .get()
        kafka.send(topic, payload);
    }
}