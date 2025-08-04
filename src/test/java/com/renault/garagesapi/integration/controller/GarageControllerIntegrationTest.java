package com.renault.garagesapi.integration.controller;

import com.renault.garagesapi.dto.GarageDto;
import com.renault.garagesapi.dto.JourHoraireDto;
import com.renault.garagesapi.dto.OpeningTimeDto;
import com.renault.garagesapi.entity.Garage;
import com.renault.garagesapi.entity.JourHoraire;
import com.renault.garagesapi.entity.OpeningTime;
import com.renault.garagesapi.entity.Vehicule;
import com.renault.garagesapi.enums.TypeCarburant;
import com.renault.garagesapi.exception.dto.ErrorResponse;
import com.renault.garagesapi.repository.GarageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class GarageControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private GarageRepository garageRepository;

    private String apiUrl;

    @BeforeEach
    void setUp() {
        apiUrl = "http://localhost:" + port + "/api/garages";
        garageRepository.deleteAll();
    }

    @Test
    @DisplayName("POST /api/garages - Doit créer un garage")
    void shouldCreateGarage() {

        GarageDto garageDto = createGarageDto();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<GarageDto> request = new HttpEntity<>(garageDto, headers);

        ResponseEntity<GarageDto> response = restTemplate.postForEntity(apiUrl, request, GarageDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().id()).isNotNull();
        assertThat(response.getBody().name()).isEqualTo("Garage Casablanca");
        assertThat(response.getBody().city()).isEqualTo("Casablanca");
        assertThat(response.getBody().address()).isEqualTo("44 Ain sebaa");
        assertThat(response.getBody().telephone()).isEqualTo("0567669786");
        assertThat(response.getBody().email()).isEqualTo("casa@renoult.ma");
        assertThat(response.getBody().horairesOuverture()).hasSize(1);

        Garage garage = garageRepository.findById(response.getBody().id()).get();
        assertThat(garage).isNotNull();
        assertThat(garage.getName()).isEqualTo("Garage Casablanca");
    }

    @Test
    @DisplayName("POST /api/garages - Doit retourner 400 (données invalides)")
    void shouldReturnInvalidData() {

        GarageDto invalidGarageDto = new GarageDto(
                null,
                "",
                "",
                "",
                "",
                "emailIinvalide",
                null
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<GarageDto> request = new HttpEntity<>(invalidGarageDto, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, request, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        // Vérifier qu'aucun garage n'a été créé
        List<Garage> garages = garageRepository.findAll();
        assertThat(garages).isEmpty();
    }

    @Test
    @DisplayName("GET /api/garages/{idGarage} - Doit récupérer un garage par son ID")
    void shouldGetGarageById() {

        Garage savedGarage = createAndSaveGarage("Garage Test");

        Garage test = garageRepository.findById(savedGarage.getId()).get();

        String url = apiUrl + "/" + savedGarage.getId();
        ResponseEntity<GarageDto> response = restTemplate.getForEntity(url, GarageDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().id()).isEqualTo(savedGarage.getId());
        assertThat(response.getBody().name()).isEqualTo("Garage Test");
        assertThat(response.getBody().city()).isEqualTo("Test");
        assertThat(response.getBody().address()).isEqualTo("Test adress");
        assertThat(response.getBody().telephone()).isEqualTo("0585655214");
        assertThat(response.getBody().email()).isEqualTo("test@garage.ma");
    }

    @Test
    @DisplayName("GET /api/garages/{idGarage} - Doit retourner 404 pour un garage inexistant")
    void shouldReturnGarageNotFound() {

        String url = apiUrl + "/15";
        ResponseEntity<ErrorResponse> response = restTemplate.getForEntity(url, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().message()).isEqualTo("Garage non trouvé");
    }

    @Test
    @DisplayName("DELETE /api/garages/{idGarage} - Doit retourner 400 pour un garage avec véhicules")
    void shouldReturn400WhenDeletingGarageWithVehicules() {

        Garage savedGarage = createAndSaveGarageWithVehicules();

        String url = apiUrl + "/" + savedGarage.getId();
        ResponseEntity<ErrorResponse> response = restTemplate.exchange(url, HttpMethod.DELETE, null, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().message()).isEqualTo("Impossible de supprimer un garage contenant des véhicules");

        // Vérifier que le garage existe toujours en base de données
        assertThat(garageRepository.findById(savedGarage.getId())).isPresent();
    }

    @Test
    @DisplayName("DELETE /api/garages/{idGarage} - Doit retourner 404 pour un garage inexistant")
    void shouldReturn404WhenDeletingNonExistentGarage() {

        String url = apiUrl + "/20";
        ResponseEntity<ErrorResponse> response = restTemplate.exchange(url, HttpMethod.DELETE, null, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().message()).isEqualTo("Garage non trouvé");
    }

    private GarageDto createGarageDto() {

        List<OpeningTimeDto> creneaux = new ArrayList<>();
        creneaux.add(new OpeningTimeDto(LocalTime.of(8, 0), LocalTime.of(12, 30)));
        creneaux.add(new OpeningTimeDto(LocalTime.of(14, 0), LocalTime.of(18, 30)));

        List<JourHoraireDto> horaires = List.of(
                new JourHoraireDto(DayOfWeek.MONDAY, creneaux)
        );

        return new GarageDto(
                null,
                "Garage Casablanca",
                "Casablanca",
                "44 Ain sebaa",
                "0567669786",
                "casa@renoult.ma",
                horaires
        );
    }

    @Transactional
    public Garage createAndSaveGarage(String name) {
        return garageRepository.save(createGarage(name));
    }

    public Garage createGarage(String name) {

        Garage garage = new Garage();
        garage.setName(name);
        garage.setAddress("Test adress");
        garage.setCity("Test");
        garage.setTelephone("0585655214");
        garage.setEmail("test@garage.ma");

        List<OpeningTime> creneaux = new ArrayList<>();
        creneaux.add(new OpeningTime(LocalTime.of(8, 0), LocalTime.of(12, 30)));
        creneaux.add(new OpeningTime(LocalTime.of(14, 0), LocalTime.of(18, 30)));

        List<JourHoraire> horaires = new ArrayList<>();
        JourHoraire lundi = new JourHoraire();
        lundi.setJour(DayOfWeek.MONDAY);
        lundi.setCreneaux(creneaux);

        garage.setHorairesOuverture(horaires);

        return garage;
    }

    private Garage createAndSaveGarageWithVehicules() {

        Garage garage = createGarage("Garage Test with vehicules");

        Vehicule vehicule = new Vehicule();
        vehicule.setBrand("Clio 4");
        vehicule.setAnneeFabrication(Year.of(2017));
        vehicule.setTypeCarburant(TypeCarburant.DIESEL);
        vehicule.setGarage(garage);

        garage.setVehicules(List.of(vehicule));
        return garageRepository.save(garage);
    }
}