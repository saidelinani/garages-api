package com.renault.garagesapi.services.impl;

import com.renault.garagesapi.dtos.VehicleDto;
import com.renault.garagesapi.entities.Vehicle;
import com.renault.garagesapi.kafka.producer.VehiculeEventsPublisher;
import com.renault.garagesapi.exceptions.GarageFullException;
import com.renault.garagesapi.exceptions.ResourceNotFoundException;
import com.renault.garagesapi.mappers.VehicleMapper;
import com.renault.garagesapi.repositories.VehicleRepository;
import com.renault.garagesapi.services.IGarageService;
import com.renault.garagesapi.services.IVehicleService;
import org.springframework.data.repository.support.Repositories;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VehicleServiceImpl implements IVehicleService {

    public static final int MAX_VEHICLES_PER_GARAGE = 2;

    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;
    private final IGarageService garageService;
    private final VehiculeEventsPublisher publisher;

    public VehicleServiceImpl(VehicleRepository vehicleRepository, VehicleMapper vehicleMapper,
                              IGarageService garageService, VehiculeEventsPublisher publisher, Repositories repositories) {
        this.vehicleRepository = vehicleRepository;
        this.vehicleMapper = vehicleMapper;
        this.garageService = garageService;
        this.publisher = publisher;
    }

    @Override
    @Transactional
    public VehicleDto addVehicule(VehicleDto vehicleDto) {

        Vehicle vehicle = vehicleMapper.toEntity(vehicleDto);
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        VehicleDto savedVehicleDto = vehicleMapper.toDto(savedVehicle);

        // Publier l'evenement
        publisher.publishVehiculeCreated(savedVehicleDto);

        return savedVehicleDto;
    }

    @Override
    @Transactional
    public VehicleDto addVehiculeToGarage(Long garageId, VehicleDto vehicleDto) {

        var garage = garageService.findGarageById(garageId);

        if (garage.getVehicles().size() >= MAX_VEHICLES_PER_GARAGE) {
            throw new GarageFullException(String.format("Le garage a atteint sa capacité maximale de %d véhicules", MAX_VEHICLES_PER_GARAGE));
        }

        Vehicle vehicle = vehicleMapper.toEntity(vehicleDto);
        vehicle.setGarage(garage);

        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        VehicleDto savedVehicleDto = vehicleMapper.toDto(savedVehicle);

        // Publier l'evenement
        publisher.publishVehiculeCreated(savedVehicleDto);

        return savedVehicleDto;
    }

    @Override
    @Transactional
    public VehicleDto updateVehicule(Long id, VehicleDto vehicleDto) {

        Vehicle vehicle = vehicleMapper.toEntity(vehicleDto);
        vehicle.setId(id);

        return vehicleMapper.toDto(vehicleRepository.save(vehicle));
    }

    @Override
    @Transactional
    public void deleteVehicule(Long id) {
        if (!vehicleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Véhicule non trouvé");
        }
        vehicleRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public VehicleDto getVehiculeById(Long id) {
        var vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Véhicule non trouvé"));
        return vehicleMapper.toDto(vehicle);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehicleDto> getVehiculesByGarage(Long garageId) {
        List<Vehicle> vehicles = vehicleRepository.findByGarageId(garageId);

        return vehicles.stream()
                        .map(vehicleMapper::toDto)
                        .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehicleDto> getVehiculesByBrand(String modele) {
        List<Vehicle> vehicles = vehicleRepository.findByBrand(modele);

        return vehicles.stream()
                .map(vehicleMapper::toDto)
                .collect(Collectors.toList());
    }
}
