package com.renault.garagesapi.tests.service;

import com.renault.garagesapi.dtos.VehicleDto;
import com.renault.garagesapi.entities.Garage;
import com.renault.garagesapi.entities.Vehicle;
import com.renault.garagesapi.enums.FuelType;
import com.renault.garagesapi.exceptions.GarageFullException;
import com.renault.garagesapi.exceptions.ResourceNotFoundException;
import com.renault.garagesapi.kafka.producer.VehiculeEventsPublisher;
import com.renault.garagesapi.mappers.VehicleMapper;
import com.renault.garagesapi.repositories.VehicleRepository;
import com.renault.garagesapi.services.IGarageService;
import com.renault.garagesapi.services.impl.VehicleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VehiculeServiceImplTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private VehicleMapper vehicleMapper;

    @Mock
    private IGarageService garageService;

    @Mock
    private VehiculeEventsPublisher publisher;

    @InjectMocks
    private VehicleServiceImpl vehiculeService;

    private VehicleDto vehicleDto;
    private Vehicle vehicle;
    private Garage garage;

    public static final int MAX_VEHICULES_PAR_GARAGE = 2;

    @BeforeEach
    void setUp() {
        // Création des objets de test
        vehicleDto = new VehicleDto(
                null,
                "Golf 8",
                Year.of(2018),
                FuelType.ESSENCE,
        null);

        vehicle = new Vehicle();
        vehicle.setId(1L);
        vehicle.setBrand("Golf 8");
        vehicle.setYearOfManufacture(Year.of(2018));
        vehicle.setFuelType(FuelType.ESSENCE);

        garage = new Garage();
        garage.setId(1L);
        garage.setName("Garage ain sebaa");
        garage.setVehicles(new ArrayList<>());
    }

    @Test
    void addVehicule_ShouldReturnSavedVehicule() {

        when(vehicleMapper.toEntity(vehicleDto)).thenReturn(vehicle);
        when(vehicleRepository.save(vehicle)).thenReturn(vehicle);
        when(vehicleMapper.toDto(vehicle)).thenReturn(vehicleDto);

        VehicleDto result = vehiculeService.addVehicule(vehicleDto);

        assertNotNull(result);
        assertEquals(vehicleDto.id(), result.id());
        assertEquals(vehicleDto.brand(), result.brand());
        assertEquals(vehicleDto.yearOfManufacture(), result.yearOfManufacture());
        assertEquals(vehicleDto.fuelType(), result.fuelType());

        verify(vehicleMapper).toEntity(vehicleDto);
        verify(vehicleRepository).save(vehicle);
        verify(vehicleMapper).toDto(vehicle);
        verify(publisher).publishVehiculeCreated(vehicleDto);
    }


    @Test
    void addVehiculeToGarage_ShouldReturnSavedVehicule() {

        Long garageId = 1L;
        when(garageService.findGarageById(garageId)).thenReturn(garage);
        when(vehicleMapper.toEntity(vehicleDto)).thenReturn(vehicle);
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(vehicle);
        when(vehicleMapper.toDto(vehicle)).thenReturn(vehicleDto);

        VehicleDto result = vehiculeService.addVehiculeToGarage(garageId, vehicleDto);

        assertNotNull(result);
        assertEquals(vehicleDto.id(), result.id());
        verify(garageService).findGarageById(garageId);
        verify(vehicleMapper).toEntity(vehicleDto);
        verify(vehicleRepository).save(any(Vehicle.class));
        verify(vehicleMapper).toDto(vehicle);

        assertEquals(garage, vehicle.getGarage());
    }

    @Test
    void addVehiculeToGarage_ShouldThrowExceptionWhenGarageIsFull() {

        Long garageId = 1L;
        List<Vehicle> vehiculesExistants = Arrays.asList(new Vehicle(), new Vehicle());
        garage.setVehicles(vehiculesExistants);

        when(garageService.findGarageById(garageId)).thenReturn(garage);

        assertThatExceptionOfType(GarageFullException.class)
                .isThrownBy(() -> vehiculeService.addVehiculeToGarage(garageId, vehicleDto))
                .withMessage("Le garage a atteint sa capacité maximale de "+MAX_VEHICULES_PAR_GARAGE+" véhicules");

        verify(garageService).findGarageById(garageId);
        verify(vehicleMapper, never()).toEntity(any());
        verify(vehicleRepository, never()).save(any());
    }

    @Test
    void deleteVehicule() {

        Long vehiculeId = 1L;

        when(vehicleRepository.existsById(vehiculeId)).thenReturn(true);

        vehiculeService.deleteVehicule(vehiculeId);

        verify(vehicleRepository).existsById(vehiculeId);
        verify(vehicleRepository).deleteById(vehiculeId);
    }

    @Test
    void getVehiculeById_ShouldReturnVehicule() {

        Long vehiculeId = 1L;
        when(vehicleRepository.findById(vehiculeId)).thenReturn(Optional.of(vehicle));
        when(vehicleMapper.toDto(vehicle)).thenReturn(vehicleDto);

        VehicleDto result = vehiculeService.getVehiculeById(vehiculeId);

        assertNotNull(result);
        assertEquals(vehicleDto.id(), result.id());
        assertEquals(vehicleDto.brand(), result.brand());
        assertEquals(vehicleDto.yearOfManufacture(), result.yearOfManufacture());
        assertEquals(vehicleDto.fuelType(), result.fuelType());

        verify(vehicleRepository).findById(vehiculeId);
        verify(vehicleMapper).toDto(vehicle);
    }

    @Test
    void getVehiculeById_ShouldThrowResourceNotFoundException() {

        Long vehiculeId = 2000L;
        when(vehicleRepository.findById(vehiculeId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> vehiculeService.getVehiculeById(vehiculeId));

        assertEquals("Véhicule non trouvé", exception.getMessage());

        verify(vehicleRepository).findById(vehiculeId);
        verify(vehicleMapper, never()).toDto(any());
    }

    @Test
    void getVehiculesByGarage_ShouldReturnVehiculeList() {

        Long garageId = 1L;
        List<Vehicle> vehicles = Arrays.asList(vehicle);

        when(vehicleRepository.findByGarageId(garageId)).thenReturn(vehicles);
        when(vehicleMapper.toDto(vehicle)).thenReturn(vehicleDto);

        List<VehicleDto> result = vehiculeService.getVehiculesByGarage(garageId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(vehicleDto.id(), result.get(0).id());

        verify(vehicleRepository).findByGarageId(garageId);
        verify(vehicleMapper).toDto(any(Vehicle.class));
    }

    @Test

    void getVehiculesByGarage_ShouldReturnEmptyListWhenGarageHasNoVehicules() {
        Long garageId = 1L;
        when(vehicleRepository.findByGarageId(garageId)).thenReturn(new ArrayList<>());

        List<VehicleDto> result = vehiculeService.getVehiculesByGarage(garageId);

        assertNotNull(result);
        assertEquals(0, result.size());

        verify(vehicleRepository).findByGarageId(garageId);
        verify(vehicleMapper, never()).toDto(any());
    }


    @Test
    void updateVehicule() {

        Long vehiculeId = 1L;
        Vehicle updatedVehicle = createVehicule();
        updatedVehicle.setId(vehiculeId);

        Vehicle savedVehicle = createVehicule();
        savedVehicle.setId(vehiculeId);

        when(vehicleMapper.toEntity(vehicleDto)).thenReturn(updatedVehicle);
        when(vehicleRepository.save(updatedVehicle)).thenReturn(savedVehicle);
        when(vehicleMapper.toDto(savedVehicle)).thenReturn(vehicleDto);

        VehicleDto result = vehiculeService.updateVehicule(vehiculeId, vehicleDto);

        assertThat(result).isNotNull();
        assertThat(updatedVehicle.getId()).isEqualTo(vehiculeId);

        verify(vehicleMapper).toEntity(vehicleDto);
        verify(vehicleRepository).save(updatedVehicle);
        verify(vehicleMapper).toDto(savedVehicle);
    }

    @Test
    void getVehiculesByModele() {

        String brand = "Golf 8";
        List<Vehicle> vehicles = Arrays.asList(vehicle);

        when(vehicleRepository.findByBrand(brand)).thenReturn(vehicles);
        when(vehicleMapper.toDto(vehicle)).thenReturn(vehicleDto);

        List<VehicleDto> result = vehiculeService.getVehiculesByBrand(brand);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(vehicleDto.id(), result.get(0).id());

        verify(vehicleRepository).findByBrand(brand);
        verify(vehicleMapper).toDto(any(Vehicle.class));
    }

    private Vehicle createVehicule() {

        Vehicle vehicle = new Vehicle();
        vehicle.setId(1L);
        vehicle.setBrand("Golf 8");
        vehicle.setYearOfManufacture(Year.of(2018));
        vehicle.setFuelType(FuelType.ESSENCE);

        return vehicle;
    }
}
