package com.renault.garagesapi.service.impl;

import com.renault.garagesapi.dto.VehiculeDto;
import com.renault.garagesapi.entity.Garage;
import com.renault.garagesapi.entity.Vehicule;
import com.renault.garagesapi.exception.GarageFullException;
import com.renault.garagesapi.exception.ResourceNotFoundException;
import com.renault.garagesapi.mapper.GarageMapper;
import com.renault.garagesapi.mapper.VehiculeMapper;
import com.renault.garagesapi.repository.VehiculeRepository;
import com.renault.garagesapi.service.IGarageService;
import com.renault.garagesapi.service.IVehiculeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VehiculeServiceImpl implements IVehiculeService {

    public static final int MAX_VEHICULES_PAR_GARAGE = 2;

    private final VehiculeRepository vehiculeRepository;
    private final VehiculeMapper vehiculeMapper;
    private final IGarageService garageService;
    private final GarageMapper  garageMapper;

    public VehiculeServiceImpl(VehiculeRepository vehiculeRepository, VehiculeMapper vehiculeMapper,
		    IGarageService garageService, GarageMapper garageMapper) {
        this.vehiculeRepository = vehiculeRepository;
        this.vehiculeMapper = vehiculeMapper;
	this.garageService = garageService;
	this.garageMapper = garageMapper;
    }

    @Override
    @Transactional
    public VehiculeDto addVehicule(VehiculeDto vehiculeDto) {

        Vehicule vehicule = vehiculeMapper.toEntity(vehiculeDto);
        Vehicule savedVehicule = vehiculeRepository.save(vehicule);
        return vehiculeMapper.toDto(savedVehicule);
    }

    @Override
    @Transactional
    public VehiculeDto addVehiculeToGarage(Long garageId, VehiculeDto vehiculeDto) {

        Garage garage = garageService.findGarageById(garageId);

        if (garage.getVehicules().size() >= MAX_VEHICULES_PAR_GARAGE) {
            throw new GarageFullException("Le garage a atteint sa capacité maximale de 50 véhicules");
        }

        Vehicule vehicule = vehiculeMapper.toEntity(vehiculeDto);
        vehicule.setGarage(garage);

        Vehicule savedVehicule = vehiculeRepository.save(vehicule);

        return vehiculeMapper.toDto(savedVehicule);
    }

    @Override
    @Transactional
    public VehiculeDto updateVehicule(Long id, VehiculeDto vehiculeDto) {
        return null;
    }

    @Override
    @Transactional
    public void deleteVehicule(Long id) {
        vehiculeRepository.deleteById(id);
    }

    @Override
    public VehiculeDto getVehiculeById(Long id) {

        return vehiculeMapper.toDto(findVehiculeById(id));
    }

    @Override
    public List<VehiculeDto> getVehiculesByGarage(Long garageId) {
        List<Vehicule> vehicules = vehiculeRepository.findByGarageId(garageId);

        return vehicules.stream()
                        .map(vehiculeMapper::toDto)
                        .collect(Collectors.toList());
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
