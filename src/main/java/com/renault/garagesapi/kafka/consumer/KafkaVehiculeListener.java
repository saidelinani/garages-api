package com.renault.garagesapi.kafka.consumer;

import com.renault.garagesapi.dtos.VehicleDto;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaVehiculeListener {

    @KafkaListener(topics = "vehicule_creation", groupId = "groupId")
    public void kafkaListener(VehicleDto vehicleDto) {
        System.out.println("Véhicule reçu: " + vehicleDto);
    }
}
