package com.renault.garagesapi.kafka.consumer;

import com.renault.garagesapi.dto.VehiculeDto;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaVehiculeListener {

    @KafkaListener(topics = "vehicule_creation", groupId = "groupId")
    public void kafkaListener(VehiculeDto vehiculeDto) {
        System.out.println("Véhicule reçu: " + vehiculeDto);
    }
}
