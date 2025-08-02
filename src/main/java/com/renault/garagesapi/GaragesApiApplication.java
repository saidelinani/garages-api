package com.renault.garagesapi;

import com.renault.garagesapi.repository.GarageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GaragesApiApplication {

    @Autowired
    private GarageRepository garageRepository;

    public static void main(String[] args) {
        SpringApplication.run(GaragesApiApplication.class, args);
    }
}
