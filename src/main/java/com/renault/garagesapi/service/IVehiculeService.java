package com.renault.garagesapi.service;

import com.renault.garagesapi.dto.VehiculeDto;
import java.util.List;

public interface IVehiculeService {

    VehiculeDto addVehicule(VehiculeDto vehiculeDto);
    VehiculeDto addVehiculeToGarage(Long garageId, VehiculeDto vehiculeDto);
    VehiculeDto updateVehicule(Long id, VehiculeDto vehiculeDto);
    void deleteVehicule(Long id);
    VehiculeDto  getVehiculeById(Long id);
    List<VehiculeDto> getVehiculesByGarage(Long garageId);
    List<VehiculeDto> getVehiculesByBrand(String modele);
}
