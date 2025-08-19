package com.renault.garagesapi;

import com.renault.garagesapi.dto.VehicleDto;
import com.renault.garagesapi.enums.TypeCarburant;
import com.renault.garagesapi.repository.GarageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import java.time.Year;

@SpringBootApplication
public class GaragesApiApplication {

    @Autowired
    private GarageRepository garageRepository;

    public static void main(String[] args) {
        SpringApplication.run(GaragesApiApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(KafkaTemplate<Long, VehicleDto> kafkaTemplate) {
        return args -> {
            System.out.println("Nombre des vehicules: "+garageRepository.countByGarageId(1L));
            VehicleDto vehicule = new VehicleDto(
                    1L,
                    "Clio 4",
                    Year.of(2025),
                    TypeCarburant.DIESEL,
                    null
            );
            kafkaTemplate.send("vehicule_creation", vehicule);
        };
    }
}
