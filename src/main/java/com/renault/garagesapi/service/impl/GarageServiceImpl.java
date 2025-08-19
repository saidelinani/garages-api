package com.renault.garagesapi.service.impl;

import com.renault.garagesapi.dto.GarageDto;
import com.renault.garagesapi.entity.DaySchedule;
import com.renault.garagesapi.entity.Garage;
import com.renault.garagesapi.exception.GarageHasVehiclesException;
import com.renault.garagesapi.exception.ResourceNotFoundException;
import com.renault.garagesapi.mapper.GarageMapper;
import com.renault.garagesapi.repository.GarageRepository;
import com.renault.garagesapi.service.IGarageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GarageServiceImpl implements IGarageService {

    private final GarageRepository garageRepository;
    private final GarageMapper garageMapper;

    public GarageServiceImpl(GarageRepository garageRepository, GarageMapper garageMapper) {
        this.garageRepository = garageRepository;
        this.garageMapper = garageMapper;
    }

    @Override
    @Transactional
    public GarageDto addGarage(GarageDto garageDto) {

        Garage garage = garageMapper.toEntity(garageDto);

        for (DaySchedule daySchedule : garage.getHorairesOuverture()) {
            daySchedule.setGarage(garage);
        }

        Garage savedGarage = garageRepository.save(garage);
        return garageMapper.toDto(savedGarage);
    }

    @Override
    @Transactional
    public GarageDto updateGarage(Long id, GarageDto garageDto) {

        Garage existingGarage = findGarageById(id);

        Garage garage = garageMapper.toEntity(garageDto);
        garage.setId(id);
        garage.setVehicles(existingGarage.getVehicles());

        return garageMapper.toDto(garageRepository.save(garage));
    }

    @Override
    @Transactional
    public void deleteGarage(Long id) {
        var existingGarage = findGarageById(id);

        if (!existingGarage.getVehicles().isEmpty()) {
            throw new GarageHasVehiclesException("Impossible de supprimer un garage contenant des véhicules");
        }

        garageRepository.deleteById(id);
    }


    @Override
    @Transactional(readOnly = true)
    public GarageDto getGarageDtoById(Long id) {
        return garageMapper.toDto(findGarageById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GarageDto> getAllGarages(Pageable pageable) {
        return garageRepository.findAll(pageable)
                .map(garageMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public int getNombreVehicules(Long garageId) {
        return garageRepository.countByGarageId(garageId);
    }

    @Override
    @Transactional(readOnly = true)
    public Garage findGarageById(Long id) {
        return garageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Garage non trouvé"));
    }
}
