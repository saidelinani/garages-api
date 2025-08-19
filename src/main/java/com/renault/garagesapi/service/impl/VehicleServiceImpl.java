package com.renault.garagesapi.service.impl;

import com.renault.garagesapi.dto.VehicleDto;
import com.renault.garagesapi.entity.Vehicle;
import com.renault.garagesapi.kafka.producer.VehiculeEventsPublisher;
import com.renault.garagesapi.exception.GarageFullException;
import com.renault.garagesapi.exception.ResourceNotFoundException;
import com.renault.garagesapi.mapper.VehicleMapper;
import com.renault.garagesapi.repository.VehicleRepository;
import com.renault.garagesapi.service.IGarageService;
import com.renault.garagesapi.service.IVehicleService;
import org.springframework.data.repository.support.Repositories;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VehicleServiceImpl implements IVehicleService {

    public static final int MAX_VEHICULES_PAR_GARAGE = 2;

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

        if (garage.getVehicles().size() >= MAX_VEHICULES_PAR_GARAGE) {
            throw new GarageFullException("Le garage a atteint sa capacité maximale de "+MAX_VEHICULES_PAR_GARAGE+" véhicules");
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
    public VehicleDto getVehiculeById(Long id) {

        return vehicleMapper.toDto(findVehiculeById(id));
    }

    @Override
    public List<VehicleDto> getVehiculesByGarage(Long garageId) {
        List<Vehicle> vehicles = vehicleRepository.findByGarageId(garageId);

        return vehicles.stream()
                        .map(vehicleMapper::toDto)
                        .collect(Collectors.toList());
    }

    @Override
    public List<VehicleDto> getVehiculesByBrand(String modele) {
        List<Vehicle> vehicles = vehicleRepository.findByBrand(modele);

        return vehicles.stream()
                .map(vehicleMapper::toDto)
                .collect(Collectors.toList());
    }

    private Vehicle findVehiculeById(Long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Véhicule non trouvé"));
    }
}
