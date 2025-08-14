package com.renault.garagesapi.test.service;

import com.renault.garagesapi.dto.GarageDto;
import com.renault.garagesapi.dto.JourHoraireDto;
import com.renault.garagesapi.dto.OpeningTimeDto;
import com.renault.garagesapi.entity.Garage;
import com.renault.garagesapi.entity.JourHoraire;
import com.renault.garagesapi.entity.OpeningTime;
import com.renault.garagesapi.entity.Vehicule;
import com.renault.garagesapi.exception.GarageHasVehiclesException;
import com.renault.garagesapi.exception.ResourceNotFoundException;
import com.renault.garagesapi.mapper.GarageMapper;
import com.renault.garagesapi.repository.GarageRepository;
import com.renault.garagesapi.service.impl.GarageServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class GarageServiceImplTest {

    @Mock
    private GarageRepository garageRepository;

    @Mock
    private GarageMapper garageMapper;

    @InjectMocks
    private GarageServiceImpl garageService;

    @Test
    @DisplayName("Ajout d’un garage")
    void shouldAddGarageWithHoraires() {

        GarageDto garageDto = createGarageDto();
        Garage garage = createGarage();
        Garage savedGarage = createGarage();
        savedGarage.setId(1L);

        when(garageMapper.toEntity(garageDto)).thenReturn(garage);
        when(garageRepository.save(garage)).thenReturn(savedGarage);
        when(garageMapper.toDto(savedGarage)).thenReturn(garageDto);

        GarageDto result = garageService.addGarage(garageDto);

        assertThat(result).isNotNull();
        verify(garageMapper).toEntity(garageDto);
        verify(garageRepository).save(garage);
        verify(garageMapper).toDto(savedGarage);
    }

    @Test
    @DisplayName("Mettre à jour un garage")
    void shouldUpdateExistingGarage() {

        Long garageId = 1L;
        GarageDto garageDto = createGarageDto();
        Garage existingGarage = createGarage();
        existingGarage.setId(garageId);

        Garage updatedGarage = createGarage();
        updatedGarage.setId(garageId);

        Garage savedGarage = createGarage();
        savedGarage.setId(garageId);

        when(garageRepository.findById(garageId)).thenReturn(Optional.of(existingGarage));
        when(garageMapper.toEntity(garageDto)).thenReturn(updatedGarage);
        when(garageRepository.save(updatedGarage)).thenReturn(savedGarage);
        when(garageMapper.toDto(savedGarage)).thenReturn(garageDto);

        GarageDto result = garageService.updateGarage(garageId, garageDto);

        assertThat(result).isNotNull();
        assertThat(updatedGarage.getId()).isEqualTo(garageId);
        assertThat(updatedGarage.getVehicules()).isEqualTo(existingGarage.getVehicules());

        verify(garageRepository).findById(garageId);
        verify(garageMapper).toEntity(garageDto);
        verify(garageRepository).save(updatedGarage);
        verify(garageMapper).toDto(savedGarage);
    }

    @Test
    @DisplayName("Doit lever une exception si le garage n'existe pas")
    void shouldThrowExceptionWhenGarageNotFound() {

        Long garageId = 2L;
        GarageDto garageDto = createGarageDto();

        when(garageRepository.findById(garageId)).thenReturn(Optional.empty());

        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> garageService.updateGarage(garageId, garageDto))
                .withMessage("Garage non trouvé");

        verify(garageRepository).findById(garageId);
        verify(garageMapper, never()).toEntity(any());
        verify(garageRepository, never()).save(any());
    }

    @Test
    @DisplayName("Doit supprimer un garage")
    void shouldDeleteGarage() {

        Long garageId = 1L;
        Garage garage = createGarage();
        garage.setId(garageId);
        garage.setVehicules(new ArrayList<>());

        when(garageRepository.findById(garageId)).thenReturn(Optional.of(garage));

        garageService.deleteGarage(garageId);

        verify(garageRepository).findById(garageId);
        verify(garageRepository).deleteById(garageId);
    }

    @Test
    @DisplayName("Doit empêcher la suppression d’un garage avec des véhicules associés")
    void shouldThrowExceptionWhenDeletingGarageWithVehicules() {

        Long garageId = 1L;
        Garage garage = createGarage();
        garage.setId(garageId);
        garage.setVehicules(List.of(new Vehicule(), new Vehicule()));

        when(garageRepository.findById(garageId)).thenReturn(Optional.of(garage));

        assertThatExceptionOfType(GarageHasVehiclesException.class)
                .isThrownBy(() -> garageService.deleteGarage(garageId))
                .withMessage("Impossible de supprimer un garage contenant des véhicules");

        verify(garageRepository).findById(garageId);
        verify(garageRepository, never()).deleteById(anyLong());
    }


    @Test
    @DisplayName("Récupérer un garage par son ID")
    void shouldGetGarageById() {

        Long garageId = 1L;
        Garage garage = createGarage();
        garage.setId(garageId);
        GarageDto garageDto = createGarageDto();

        when(garageRepository.findById(garageId)).thenReturn(Optional.of(garage));
        when(garageMapper.toDto(garage)).thenReturn(garageDto);

        GarageDto result = garageService.getGarageById(garageId);

        assertThat(result).isNotNull();
        verify(garageRepository).findById(garageId);
        verify(garageMapper).toDto(garage);
    }

    @Test
    @DisplayName("Doit lever une exception lors de la récupération d'un garage inexistant")
    void shouldThrowExceptionWhenGettingNonExistentGarage() {

        Long garageId = 2L;

        when(garageRepository.findById(garageId)).thenReturn(Optional.empty());

        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> garageService.deleteGarage(garageId))
                .withMessage("Garage non trouvé");

        verify(garageRepository).findById(garageId);
        verify(garageMapper, never()).toDto(any());
    }

    @Test
    @DisplayName("Doit récupérer tous les garages")
    void shouldGetAllGaragesWithPagination() {

        Pageable pageable = PageRequest.of(0, 10);
        List<Garage> garages = List.of(createGarage(), createGarage());
        Page<Garage> garagePage = new PageImpl<>(garages, pageable, garages.size());
        GarageDto garageDto = createGarageDto();

        when(garageRepository.findAll(pageable)).thenReturn(garagePage);
        when(garageMapper.toDto(any(Garage.class))).thenReturn(garageDto);

        Page<GarageDto> result = garageService.getAllGarages(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2);
    }

    @Test
    @DisplayName("Doit retourner le nombre de véhicules")
    void shouldGetNombreVehicules() {

        Long garageId = 1L;
        Garage garage = createGarage();

        when(garageRepository.findById(garageId)).thenReturn(Optional.of(garage));

        int result = garageService.getNombreVehicules(garageId);

        assertThat(result).isEqualTo(0);
        verify(garageRepository).findById(garageId);
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

    private Garage createGarage() {

        Garage garage = new Garage();
        garage.setName("Garage Casablanca");
        garage.setAddress("44 Ain sebaa");
        garage.setCity("Casablanca");
        garage.setTelephone("0567669786");
        garage.setEmail("casa@renoult.ma");

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
}
