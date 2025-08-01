package com.renault.garagesapi.service.impl;

import com.renault.garagesapi.dto.VehiculeDto;
import com.renault.garagesapi.service.IVehiculeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehiculeServiceImpl implements IVehiculeService {
    @Override
    public VehiculeDto addVehicule(VehiculeDto vehiculeDto) {
        return null;
    }

    @Override
    public VehiculeDto addVehiculeToGarage(Long garageId, VehiculeDto vehiculeDto) {
        return null;
    }

    @Override
    public VehiculeDto updateVehicule(Long id, VehiculeDto vehiculeDto) {
        return null;
    }

    @Override
    public void deleteVehicule(Long id) {

    }

    @Override
    public VehiculeDto getVehiculeById(Long id) {
        return null;
    }

    @Override
    public List<VehiculeDto> getVehiculesByGarage(Long garageId) {
        return List.of();
    }

    @Override
    public List<VehiculeDto> getVehiculesByModele(String modele) {
        return List.of();
    }
}
