package com.renault.garagesapi.integration.controllers;

import com.renault.garagesapi.entities.DaySchedule;
import com.renault.garagesapi.entities.Garage;
import com.renault.garagesapi.entities.OpeningTime;
import com.renault.garagesapi.entities.Vehicle;
import com.renault.garagesapi.enums.FuelType;
import com.renault.garagesapi.repositories.GarageRepository;
import com.renault.garagesapi.repositories.VehicleRepository;
import com.renault.garagesapi.tools.JsonReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class GarageControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GarageRepository garageRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @BeforeEach
    @Transactional
    void setUp() {
        vehicleRepository.deleteAll();
        garageRepository.deleteAll();
    }

    @Test
    @DisplayName("POST /api/garages - Doit créer un garage")
    void shouldCreateGarage() throws Exception {

        String requestJson = JsonReader.loadJsonFromResources("requests/create-garage-request.json");
        String expectedJson = JsonReader.loadJsonFromResources("responses/created-garage-response.json");

        MvcResult result = mockMvc.perform(post("/api/garages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        JSONAssert.assertEquals(expectedJson, responseBody, JSONCompareMode.LENIENT);

        // Check in database
        List<Garage> garages = garageRepository.findAll();
        assertThat(garages).hasSize(1);
    }

    @Test
    @DisplayName("POST /api/garages - Doit retourner 400 (données invalides)")
    void shouldReturnInvalidData() throws Exception {

        String requestJson = JsonReader.loadJsonFromResources("requests/create-garage-invalid-data-request.json");
        String expectedJson = JsonReader.loadJsonFromResources("errors/create-garage-invalid-data-response.json");

        MvcResult result = mockMvc.perform(post("/api/garages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        JSONAssert.assertEquals(expectedJson, responseBody, JSONCompareMode.LENIENT);

        // Check that no garage has been created
        List<Garage> garages = garageRepository.findAll();
        assertThat(garages).isEmpty();
    }

    @Test
    @DisplayName("GET /api/garages/{idGarage} - Doit récupérer un garage par son ID")
    void shouldGetGarageById() throws Exception {

        Garage savedGarage = createAndSaveGarage("Garage Test");

        MvcResult result = mockMvc.perform(get("/api/garages/{idGarage}", savedGarage.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        String expectedJson = JsonReader.loadJsonWithPlaceholders(
                "responses/get-garage-response.json",
                Map.of("id", savedGarage.getId().toString())
        );

        JSONAssert.assertEquals(expectedJson, responseBody, JSONCompareMode.STRICT);
    }

    @Test
    @DisplayName("GET /api/garages/{idGarage} - Doit retourner 404 pour un garage inexistant")
    void shouldReturnGarageNotFound() throws Exception {

        MvcResult result = mockMvc.perform(get("/api/garages/{idGarage}", 20L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        String expectedJson = JsonReader.loadJsonFromResources("errors/garage-not-found.json");
        JSONAssert.assertEquals(expectedJson, responseBody, JSONCompareMode.LENIENT);
    }

    @Test
    @DisplayName("DELETE /api/garages/{idGarage} - Doit retourner 400 pour un garage avec véhicules")
    void shouldReturn400WhenDeletingGarageWithVehicules() throws Exception {

        Garage savedGarage = createAndSaveGarageWithVehicules();

        MvcResult result = mockMvc.perform(delete("/api/garages/{idGarage}", savedGarage.getId()))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        String expectedJson = JsonReader.loadJsonFromResources("errors/cannot-delete-garage-with-vehicles.json");
        JSONAssert.assertEquals(expectedJson, responseBody, JSONCompareMode.LENIENT);

        // Check that the garage still exists in the database
        assertThat(garageRepository.findById(savedGarage.getId())).isPresent();
    }

    @Test
    @DisplayName("DELETE /api/garages/{idGarage} - Doit retourner 404 pour un garage inexistant")
    void shouldReturn404WhenDeletingNonExistentGarage() throws Exception {

        MvcResult result = mockMvc.perform(delete("/api/garages/{idGarage}", 20L))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        String expectedJson = JsonReader.loadJsonFromResources("errors/garage-not-found.json");
        JSONAssert.assertEquals(expectedJson, responseBody, JSONCompareMode.LENIENT);
    }

    @Test
    @DisplayName("DELETE /api/garages/{idGarage} - Doit supprimer un garage sans véhicules")
    void shouldDeleteGarageSuccessfully() throws Exception {

        Garage savedGarage = createAndSaveGarage("Garage à supprimer");

        mockMvc.perform(delete("/api/garages/{idGarage}", savedGarage.getId()))
                .andExpect(status().isOk());

        // Check that the garage has been deleted from the database
        assertThat(garageRepository.findById(savedGarage.getId())).isEmpty();
    }

    @Test
    @DisplayName("GET /api/garages - Doit retourner une page de garages")
    void shouldReturnPageOfGarages() throws Exception {

        createAndSaveGarage("Garage 1");
        createAndSaveGarage("Garage 2");

        MvcResult result = mockMvc.perform(get("/api/garages")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        System.out.println("responseBody: " + responseBody);
        String expectedJson = JsonReader.loadJsonFromResources("responses/get-garages-page-response.json");
        JSONAssert.assertEquals(expectedJson, responseBody, JSONCompareMode.LENIENT);
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
        garage.setPhoneNumber("0585655214");
        garage.setEmail("test@garage.ma");

        List<OpeningTime> creneaux = new ArrayList<>();
        creneaux.add(new OpeningTime(LocalTime.of(8, 0), LocalTime.of(12, 30)));
        creneaux.add(new OpeningTime(LocalTime.of(14, 0), LocalTime.of(18, 30)));

        List<DaySchedule> horaires = new ArrayList<>();
        DaySchedule lundi = new DaySchedule();
        lundi.setDayOfWeek(DayOfWeek.MONDAY);
        lundi.setOpeningTimes(creneaux);

        garage.setDaySchedules(horaires);

        return garage;
    }

    @Transactional
    public Garage createAndSaveGarageWithVehicules() {

        Garage garage = createGarage("Garage Test with vehicules");

        Vehicle vehicle = new Vehicle();
        vehicle.setBrand("Clio 4");
        vehicle.setYearOfManufacture(Year.of(2017));
        vehicle.setFuelType(FuelType.DIESEL);
        vehicle.setGarage(garage);

        garage.setVehicles(List.of(vehicle));
        return garageRepository.save(garage);
    }
}