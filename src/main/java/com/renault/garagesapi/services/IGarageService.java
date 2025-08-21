package com.renault.garagesapi.service;

import com.renault.garagesapi.dto.GarageDto;
import com.renault.garagesapi.entities.Garage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IGarageService {

    GarageDto addGarage(GarageDto garage);
    GarageDto updateGarage(Long id, GarageDto garage);
    void deleteGarage(Long id);
    GarageDto getGarageDtoById(Long id);
    Garage findGarageById(Long id);
    Page<GarageDto> getAllGarages(Pageable pageable);
    int getNombreVehicules(Long garageId);
}