package com.renault.garagesapi.test.service;

import com.renault.garagesapi.dto.VehiculeDto;
import com.renault.garagesapi.entity.Garage;
import com.renault.garagesapi.entity.Vehicule;
import com.renault.garagesapi.enums.TypeCarburant;
import com.renault.garagesapi.exception.GarageFullException;
import com.renault.garagesapi.exception.ResourceNotFoundException;
import com.renault.garagesapi.kafka.producer.VehiculeEventsPublisher;
import com.renault.garagesapi.mapper.VehiculeMapper;
import com.renault.garagesapi.repository.VehiculeRepository;
import com.renault.garagesapi.service.IGarageService;
import com.renault.garagesapi.service.impl.VehiculeServiceImpl;
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
    private VehiculeRepository vehiculeRepository;

    @Mock
    private VehiculeMapper vehiculeMapper;

    @Mock
    private IGarageService garageService;

    @Mock
    private VehiculeEventsPublisher publisher;

    @InjectMocks
    private VehiculeServiceImpl vehiculeService;

    private VehiculeDto vehiculeDto;
    private Vehicule vehicule;
    private Garage garage;

    public static final int MAX_VEHICULES_PAR_GARAGE = 2;

    @BeforeEach
    void setUp() {
        // Création des objets de test
        vehiculeDto = new VehiculeDto(
                null,
                "Golf 8",
                Year.of(2018),
                TypeCarburant.ESSENCE.toString(),
        null);

        vehicule = new Vehicule();
        vehicule.setId(1L);
        vehicule.setBrand("Golf 8");
        vehicule.setAnneeFabrication(Year.of(2018));
        vehicule.setTypeCarburant(TypeCarburant.ESSENCE);

        garage = new Garage();
        garage.setId(1L);
        garage.setName("Garage ain sebaa");
        garage.setVehicules(new ArrayList<>());
    }

    @Test
    void addVehicule_ShouldReturnSavedVehicule() {

        when(vehiculeMapper.toEntity(vehiculeDto)).thenReturn(vehicule);
        when(vehiculeRepository.save(vehicule)).thenReturn(vehicule);
        when(vehiculeMapper.toDto(vehicule)).thenReturn(vehiculeDto);

        VehiculeDto result = vehiculeService.addVehicule(vehiculeDto);

        assertNotNull(result);
        assertEquals(vehiculeDto.id(), result.id());
        assertEquals(vehiculeDto.brand(), result.brand());
        assertEquals(vehiculeDto.anneeFabrication(), result.anneeFabrication());
        assertEquals(vehiculeDto.typeCarburant(), result.typeCarburant());

        verify(vehiculeMapper).toEntity(vehiculeDto);
        verify(vehiculeRepository).save(vehicule);
        verify(vehiculeMapper).toDto(vehicule);
        verify(publisher).publishVehiculeCreated(vehiculeDto);
    }


    @Test
    void addVehiculeToGarage_ShouldReturnSavedVehicule() {

        Long garageId = 1L;
        when(garageService.findGarageById(garageId)).thenReturn(garage);
        when(vehiculeMapper.toEntity(vehiculeDto)).thenReturn(vehicule);
        when(vehiculeRepository.save(any(Vehicule.class))).thenReturn(vehicule);
        when(vehiculeMapper.toDto(vehicule)).thenReturn(vehiculeDto);

        VehiculeDto result = vehiculeService.addVehiculeToGarage(garageId, vehiculeDto);

        assertNotNull(result);
        assertEquals(vehiculeDto.id(), result.id());
        verify(garageService).findGarageById(garageId);
        verify(vehiculeMapper).toEntity(vehiculeDto);
        verify(vehiculeRepository).save(any(Vehicule.class));
        verify(vehiculeMapper).toDto(vehicule);

        assertEquals(garage, vehicule.getGarage());
    }

    @Test
    void addVehiculeToGarage_ShouldThrowExceptionWhenGarageIsFull() {

        Long garageId = 1L;
        List<Vehicule> vehiculesExistants = Arrays.asList(new Vehicule(), new Vehicule());
        garage.setVehicules(vehiculesExistants);

        when(garageService.findGarageById(garageId)).thenReturn(garage);

        assertThatExceptionOfType(GarageFullException.class)
                .isThrownBy(() -> vehiculeService.addVehiculeToGarage(garageId, vehiculeDto))
                .withMessage("Le garage a atteint sa capacité maximale de "+MAX_VEHICULES_PAR_GARAGE+" véhicules");

        verify(garageService).findGarageById(garageId);
        verify(vehiculeMapper, never()).toEntity(any());
        verify(vehiculeRepository, never()).save(any());
    }

    @Test
    void deleteVehicule() {

        Long vehiculeId = 1L;

        vehiculeService.deleteVehicule(vehiculeId);

        verify(vehiculeRepository).deleteById(vehiculeId);
    }

    @Test
    void getVehiculeById_ShouldReturnVehicule() {

        Long vehiculeId = 1L;
        when(vehiculeRepository.findById(vehiculeId)).thenReturn(Optional.of(vehicule));
        when(vehiculeMapper.toDto(vehicule)).thenReturn(vehiculeDto);

        VehiculeDto result = vehiculeService.getVehiculeById(vehiculeId);

        assertNotNull(result);
        assertEquals(vehiculeDto.id(), result.id());
        assertEquals(vehiculeDto.brand(), result.brand());
        assertEquals(vehiculeDto.anneeFabrication(), result.anneeFabrication());
        assertEquals(vehiculeDto.typeCarburant(), result.typeCarburant());

        verify(vehiculeRepository).findById(vehiculeId);
        verify(vehiculeMapper).toDto(vehicule);
    }

    @Test
    void getVehiculeById_ShouldThrowResourceNotFoundException() {

        Long vehiculeId = 2000L;
        when(vehiculeRepository.findById(vehiculeId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> vehiculeService.getVehiculeById(vehiculeId));

        assertEquals("Véhicule non trouvé", exception.getMessage());

        verify(vehiculeRepository).findById(vehiculeId);
        verify(vehiculeMapper, never()).toDto(any());
    }

    @Test
    void getVehiculesByGarage_ShouldReturnVehiculeList() {

        Long garageId = 1L;
        List<Vehicule> vehicules = Arrays.asList(vehicule);

        when(vehiculeRepository.findByGarageId(garageId)).thenReturn(vehicules);
        when(vehiculeMapper.toDto(vehicule)).thenReturn(vehiculeDto);

        List<VehiculeDto> result = vehiculeService.getVehiculesByGarage(garageId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(vehiculeDto.id(), result.get(0).id());

        verify(vehiculeRepository).findByGarageId(garageId);
        verify(vehiculeMapper).toDto(any(Vehicule.class));
    }

    @Test

    void getVehiculesByGarage_ShouldReturnEmptyListWhenGarageHasNoVehicules() {
        Long garageId = 1L;
        when(vehiculeRepository.findByGarageId(garageId)).thenReturn(new ArrayList<>());

        List<VehiculeDto> result = vehiculeService.getVehiculesByGarage(garageId);

        assertNotNull(result);
        assertEquals(0, result.size());

        verify(vehiculeRepository).findByGarageId(garageId);
        verify(vehiculeMapper, never()).toDto(any());
    }


    @Test
    void updateVehicule() {

        Long vehiculeId = 1L;
        Vehicule updatedVehicule = createVehicule();
        updatedVehicule.setId(vehiculeId);

        Vehicule savedVehicule = createVehicule();
        savedVehicule.setId(vehiculeId);

        when(vehiculeMapper.toEntity(vehiculeDto)).thenReturn(updatedVehicule);
        when(vehiculeRepository.save(updatedVehicule)).thenReturn(savedVehicule);
        when(vehiculeMapper.toDto(savedVehicule)).thenReturn(vehiculeDto);

        VehiculeDto result = vehiculeService.updateVehicule(vehiculeId, vehiculeDto);

        assertThat(result).isNotNull();
        assertThat(updatedVehicule.getId()).isEqualTo(vehiculeId);

        verify(vehiculeMapper).toEntity(vehiculeDto);
        verify(vehiculeRepository).save(updatedVehicule);
        verify(vehiculeMapper).toDto(savedVehicule);
    }

    @Test
    void getVehiculesByModele() {

        String brand = "Golf 8";
        List<Vehicule> vehicules = Arrays.asList(vehicule);

        when(vehiculeRepository.findByBrand(brand)).thenReturn(vehicules);
        when(vehiculeMapper.toDto(vehicule)).thenReturn(vehiculeDto);

        List<VehiculeDto> result = vehiculeService.getVehiculesByModele(brand);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(vehiculeDto.id(), result.get(0).id());

        verify(vehiculeRepository).findByBrand(brand);
        verify(vehiculeMapper).toDto(any(Vehicule.class));
    }

    private Vehicule createVehicule() {

        Vehicule vehicule = new Vehicule();
        vehicule.setId(1L);
        vehicule.setBrand("Golf 8");
        vehicule.setAnneeFabrication(Year.of(2018));
        vehicule.setTypeCarburant(TypeCarburant.ESSENCE);

        return vehicule;
    }
}
