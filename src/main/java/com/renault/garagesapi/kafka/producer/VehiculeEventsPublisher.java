package com.renault.garagesapi.kafka.producer;

import com.renault.garagesapi.dto.VehiculeDto;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class VehiculeEventsPublisher {

    private static final String TOPIC = "vehicule_creation";
    private final KafkaTemplate<Long, VehiculeDto> kafkaTemplate;

    public VehiculeEventsPublisher(KafkaTemplate<Long, VehiculeDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishVehiculeCreated(VehiculeDto vehiculeDto) {
        Long key = vehiculeDto.id();
        kafkaTemplate.send(TOPIC, key, vehiculeDto);
    }
}
