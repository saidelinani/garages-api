package com.renault.garagesapi.service;

import com.renault.garagesapi.dto.VehicleDto;

import java.util.List;

public interface IVehicleService {

    VehicleDto addVehicule(VehicleDto vehicleDto);
    VehicleDto addVehiculeToGarage(Long garageId, VehicleDto vehicleDto);
    VehicleDto updateVehicule(Long id, VehicleDto vehicleDto);
    void deleteVehicule(Long id);
    VehicleDto getVehiculeById(Long id);
    List<VehicleDto> getVehiculesByGarage(Long garageId);
    List<VehicleDto> getVehiculesByBrand(String modele);
}
