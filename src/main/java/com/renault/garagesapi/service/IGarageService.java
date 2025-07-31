package com.renault.garagesapi.service;

import com.renault.garagesapi.entity.Garage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface IGarageService {

    Garage saveGarage(Garage garage);
    Garage updateGarage(Long id, Garage garage);
    void deleteGarage(Long id);
    Optional<Garage> getGarageById(Long id);
    Page<Garage> getAllGarages(Pageable pageable);
    int getNombreVehicules(Long garageId);
}