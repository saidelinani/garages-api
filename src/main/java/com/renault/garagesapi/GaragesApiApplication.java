package com.renault.garagesapi;

import com.renault.garagesapi.repository.GarageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootApplication
public class GaragesApiApplication {

    @Autowired
    private GarageRepository garageRepository;

    public static void main(String[] args) {
        SpringApplication.run(GaragesApiApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(KafkaTemplate<String, String> kafkaTemplate) {
        return args -> {
            kafkaTemplate.send("vehicule_creation", "Hello Said ");
        };
    }
}
