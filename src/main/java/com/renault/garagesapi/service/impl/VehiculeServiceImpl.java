package com.renault.garagesapi.service.impl;

import com.renault.garagesapi.dto.VehiculeDto;
import com.renault.garagesapi.entity.Accessoire;
import com.renault.garagesapi.entity.Vehicule;
import com.renault.garagesapi.exception.ResourceNotFoundException;
import com.renault.garagesapi.mapper.VehiculeMapper;
import com.renault.garagesapi.repository.VehiculeRepository;
import com.renault.garagesapi.service.IVehiculeService;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class VehiculeServiceImpl implements IVehiculeService {

    private final VehiculeRepository vehiculeRepository;
    private final VehiculeMapper vehiculeMapper;

    public VehiculeServiceImpl(VehiculeRepository vehiculeRepository, VehiculeMapper vehiculeMapper) {
        this.vehiculeRepository = vehiculeRepository;
        this.vehiculeMapper = vehiculeMapper;
    }

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

        return vehiculeMapper.toDto(findVehiculeById(id));
    }

    @Override
    public List<VehiculeDto> getVehiculesByGarage(Long garageId) {
        return List.of();
    }

    @Override
    public List<VehiculeDto> getVehiculesByModele(String modele) {
        return List.of();
    }

    private Vehicule findVehiculeById(Long id) {
        return vehiculeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Véhicule non trouvé"));
    }
}
