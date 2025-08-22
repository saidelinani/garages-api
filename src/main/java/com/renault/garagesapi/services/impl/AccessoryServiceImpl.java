package com.renault.garagesapi.services.impl;

import com.renault.garagesapi.dtos.AccessoryDto;
import com.renault.garagesapi.entities.Accessory;
import com.renault.garagesapi.entities.Vehicle;
import com.renault.garagesapi.exceptions.AccessoryAlreadyAssignedException;
import com.renault.garagesapi.exceptions.ResourceNotFoundException;
import com.renault.garagesapi.mappers.AccessoryMapper;
import com.renault.garagesapi.mappers.VehicleMapper;
import com.renault.garagesapi.repositories.AccessoryRepository;
import com.renault.garagesapi.services.IAccessoryService;
import com.renault.garagesapi.services.IVehicleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccessoryServiceImpl implements IAccessoryService {

    private final AccessoryRepository accessoryRepository;
    private final AccessoryMapper accessoryMapper;
    private final IVehicleService vehiculeService;
    private final VehicleMapper vehicleMapper;

    public AccessoryServiceImpl(AccessoryRepository accessoryRepository,
                                AccessoryMapper accessoryMapper,
                                IVehicleService vehiculeService,
                                VehicleMapper vehicleMapper) {
        this.accessoryRepository = accessoryRepository;
        this.accessoryMapper = accessoryMapper;
        this.vehiculeService = vehiculeService;
        this.vehicleMapper = vehicleMapper;
    }

    @Override
    public AccessoryDto addAccessoire(AccessoryDto accessoryDto) {

        Accessory accessory = accessoryMapper.toEntity(accessoryDto);
        Accessory savedAccessory = accessoryRepository.save(accessory);
        return accessoryMapper.toDto(savedAccessory);
    }

    @Override
    @Transactional
    public AccessoryDto addAccessoireToVehicule(Long vehiculeId, AccessoryDto accessoryDto) {

        Vehicle vehicle = vehicleMapper.toEntity(vehiculeService.getVehiculeById(vehiculeId));

        Accessory accessory = accessoryMapper.toEntity(accessoryDto);
        accessory.setVehicle(vehicle);

        Accessory savedAccessory = accessoryRepository.save(accessory);

        return accessoryMapper.toDto(savedAccessory);
    }

    @Override
    @Transactional
    public AccessoryDto updateAccessoire(Long id, AccessoryDto accessoryDto) {

        Accessory existingAccessory = findAccessoireById(id);

        Accessory accessory = accessoryMapper.toEntity(accessoryDto);
        accessory.setId(id);
        accessory.setVehicle(existingAccessory.getVehicle());

        return accessoryMapper.toDto(accessoryRepository.save(accessory));
    }

    @Override
    @Transactional
    public void deleteAccessoire(Long id) {
        Accessory accessory = findAccessoireById(id);

        accessoryRepository.delete(accessory);
    }

    @Override
    public Page<AccessoryDto> getAllAccessoires(Pageable pageable) {
        return accessoryRepository.findAll(pageable)
                .map(accessoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccessoryDto> getAccessoiresByVehicule(Long vehiculeId) {

        List<Accessory> accessories = accessoryRepository.findByVehicleId(vehiculeId);

        return accessories.stream()
                .map(accessoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Accessory findAccessoireById(Long id) {
        return accessoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Accessory not found"));
    }

    @Transactional
    @Override
    public void attachAccessoireToVehicule(Long accessoireId, Long vehiculeId) {

        Vehicle vehicle = vehicleMapper.toEntity(vehiculeService.getVehiculeById(vehiculeId));

        Accessory accessory = findAccessoireById(accessoireId);

        // Vérifier si l'accessoire n'est pas déjà associé à un véhicule
        if (accessory.getVehicle() != null) {
            throw new AccessoryAlreadyAssignedException(String.format("The accessory is already associated with the vehicle with ID %d", accessory.getVehicle().getId()));
        }

        accessory.setVehicle(vehicle);

        accessoryRepository.save(accessory);
    }
}
