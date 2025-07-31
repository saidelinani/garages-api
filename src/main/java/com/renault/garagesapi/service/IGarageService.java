package com.renault.garagesapi.service;

import com.renault.garagesapi.dto.GarageDto;
import com.renault.garagesapi.entity.Garage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface IGarageService {

    GarageDto addGarage(GarageDto garage);
    GarageDto updateGarage(Long id, GarageDto garage);
    void deleteGarage(Long id);
    public GarageDto getGarageById(Long id);
    Page<GarageDto> getAllGarages(Pageable pageable);
    int getNombreVehicules(Long garageId);
}