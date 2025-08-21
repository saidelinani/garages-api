package com.renault.garagesapi.kafka.producer;

import com.renault.garagesapi.dtos.VehicleDto;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class VehiculeEventsPublisher {

    private static final String TOPIC = "vehicule_creation";
    private final KafkaTemplate<Long, VehicleDto> kafkaTemplate;

    public VehiculeEventsPublisher(KafkaTemplate<Long, VehicleDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishVehiculeCreated(VehicleDto vehicleDto) {
        Long key = vehicleDto.id();
        kafkaTemplate.send(TOPIC, key, vehicleDto);
    }
}
