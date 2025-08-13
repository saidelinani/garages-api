package com.renault.garagesapi.config;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaVehiculeListener {

    @KafkaListener(topics = "vehicule_creation", groupId = "groupId")
    public void KafkaListener(String data) {
        System.out.println(data);
    }
}
