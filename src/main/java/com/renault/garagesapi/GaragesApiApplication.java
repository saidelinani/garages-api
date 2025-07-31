package com.renault.garagesapi;

import com.renault.garagesapi.entity.*;
import com.renault.garagesapi.enums.TypeCarburant;
import com.renault.garagesapi.repository.AccessoireRepository;
import com.renault.garagesapi.repository.GarageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class GaragesApiApplication implements CommandLineRunner {

    @Autowired
    private GarageRepository garageRepository;

    public static void main(String[] args) {
        SpringApplication.run(GaragesApiApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        Garage garageCasa = new Garage();
        garageCasa.setName("Renault Ainsbaa");
        garageCasa.setAddress("JF28+FJ9, Bd Ali Yaâta, Casablanca 20250");
        garageCasa.setTelephone("0522562252");
        garageCasa.setEmail("ainsebaa@renault.ma");

        Accessoire accessoire1 = new Accessoire();
        accessoire1.setNom("Toit ouvrant");
        accessoire1.setDescription("Toit ouvrant");
        accessoire1.setPrix(new BigDecimal(10000));
        accessoire1.setType("Confort");

        Accessoire accessoire2 = new Accessoire();
        accessoire2.setNom("Caméra de recul");
        accessoire2.setDescription("Caméra de recul");
        accessoire2.setPrix(new BigDecimal(700));
        accessoire2.setType("Sécurité");

        Vehicule vehicule1 = new Vehicule();
        vehicule1.setBrand("Golf 8");
        vehicule1.setAnneeFabrication(2018);
        vehicule1.setTypeCarburant(TypeCarburant.ESSENCE);
        vehicule1.setGarage(garageCasa);
        accessoire1.setVehicule(vehicule1);
        accessoire2.setVehicule(vehicule1);
        vehicule1.setAccessoires(List.of(accessoire1, accessoire2));

        garageCasa.setVehicules(List.of(vehicule1));
        garageCasa.setHorairesOuverture(createHorairesGarage(garageCasa));

        garageRepository.save(garageCasa);
    }

    private List<JourHoraire> createHorairesGarage(Garage garage) {

        List<JourHoraire> horairesOuverture = new ArrayList<>();

        for (DayOfWeek day : Arrays.asList(DayOfWeek.MONDAY, DayOfWeek.TUESDAY,
                DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY)) {
            JourHoraire jourHoraire = new JourHoraire();
            jourHoraire.setJour(day);
            jourHoraire.setGarage(garage);

            // Créneaux matin et après-midi
            OpeningTime matin = new OpeningTime(LocalTime.of(8, 0), LocalTime.of(12, 30));
            OpeningTime apresMidi = new OpeningTime(LocalTime.of(14, 0), LocalTime.of(18, 0));

            jourHoraire.setCreneaux(Arrays.asList(matin, apresMidi));
            horairesOuverture.add(jourHoraire);
        }

        // Vendredi (8h-12h et 14h30-18h)
        JourHoraire vendredi = new JourHoraire();
        vendredi.setJour(DayOfWeek.FRIDAY);
        vendredi.setGarage(garage);
        OpeningTime matinVendredi = new OpeningTime(LocalTime.of(8, 0), LocalTime.of(12, 0));
        OpeningTime apresMidiVendredi = new OpeningTime(LocalTime.of(14, 30), LocalTime.of(18, 0));
        vendredi.setCreneaux(Arrays.asList(matinVendredi, apresMidiVendredi));
        horairesOuverture.add(vendredi);

        // Samedi (8h-13h)
        JourHoraire samedi = new JourHoraire();
        samedi.setJour(DayOfWeek.SATURDAY);
        samedi.setGarage(garage);
        OpeningTime matinSamedi = new OpeningTime(LocalTime.of(8, 0), LocalTime.of(13, 0));
        samedi.setCreneaux(Arrays.asList(matinSamedi));
        horairesOuverture.add(samedi);

        return horairesOuverture;
    }
}
