package com.renault.garagesapi.factory;

import com.renault.garagesapi.entities.DaySchedule;
import com.renault.garagesapi.entities.Garage;
import com.renault.garagesapi.entities.OpeningTime;
import com.renault.garagesapi.entities.Vehicle;
import com.renault.garagesapi.enums.FuelType;
import com.renault.garagesapi.repositories.GarageRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.Year;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class GarageTestFactory {

    private final GarageRepository garageRepository;

    public GarageTestFactory(GarageRepository garageRepository) {
        this.garageRepository = garageRepository;
    }

    @Transactional
    public Garage createAndSaveGarage(String name) {
        return garageRepository.save(createGarage(name));
    }

    public Garage createGarage(String name) {

        List<OpeningTime> openingTimes = Arrays.asList(
                new OpeningTime(LocalTime.of(8, 0), LocalTime.of(12, 30)),
                new OpeningTime(LocalTime.of(14, 0), LocalTime.of(18, 30))
        );

        DaySchedule monday = DaySchedule.builder()
                .dayOfWeek(DayOfWeek.MONDAY)
                .openingTimes(openingTimes)
                .build();

        Garage garage = Garage.builder()
                .name(name)
                .address("Test adress")
                .city("Test")
                .phoneNumber("0585655214")
                .email("test@garage.ma")
                .daySchedules(Collections.singletonList(monday))
                .build();

        monday.setGarage(garage);

        return garage;
    }


    @Transactional
    public Garage createAndSaveGarageWithVehicules() {

        Garage garage = createGarage("Garage Test with vehicules");

        Vehicle vehicle = Vehicle.builder()
                .brand("Clio 4")
                .yearOfManufacture(Year.of(2017))
                .fuelType(FuelType.DIESEL)
                .garage(garage)
                .build();

        garage.setVehicles(List.of(vehicle));

        return garageRepository.save(garage);
    }
}
