package com.renault.garagesapi.kafka.producer;

import com.renault.garagesapi.dtos.VehicleDto;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class VehicleEventsPublisher {

    private static final String TOPIC = "vehicle_creation";
    private final KafkaTemplate<Long, VehicleDto> kafkaTemplate;

    public VehicleEventsPublisher(KafkaTemplate<Long, VehicleDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishVehicleCreated(VehicleDto vehicleDto) {
        Long key = vehicleDto.id();
        kafkaTemplate.send(TOPIC, key, vehicleDto);
    }
}
