package com.renault.garagesapi.service.impl;

import com.renault.garagesapi.dto.GarageDto;
import com.renault.garagesapi.entity.Garage;
import com.renault.garagesapi.exception.BusinessException;
import com.renault.garagesapi.exception.ResourceNotFoundException;
import com.renault.garagesapi.mapper.GarageMapper;
import com.renault.garagesapi.repository.GarageRepository;
import com.renault.garagesapi.service.IGarageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class GarageServiceImpl implements IGarageService {

    private final GarageRepository garageRepository;
    private final GarageMapper garageMapper;

    public GarageServiceImpl(GarageRepository garageRepository, GarageMapper garageMapper) {
	this.garageRepository = garageRepository;
	this.garageMapper = garageMapper;
    }

    @Override public GarageDto addGarage(GarageDto garageDto) {

	Garage garage = garageMapper.toEntity(garageDto);
	Garage savedGarage = garageRepository.save(garage);
	return garageMapper.toDto(savedGarage);
    }

    @Override
    public GarageDto updateGarage(Long id, GarageDto garageDto) {

	Garage existingGarage = findGarageById(id);

	Garage garage = garageMapper.toEntity(garageDto);
	garage.setId(id);
	garage.setVehicules(existingGarage.getVehicules());

	return garageMapper.toDto(garageRepository.save(garage));
    }

    @Override public void deleteGarage(Long id) {
	Garage existingGarage = findGarageById(id);

	if (!existingGarage.getVehicules().isEmpty()) {
	    throw new BusinessException("Impossible de supprimer un garage contenant des véhicules");
	}

	garageRepository.deleteById(id);
    }


    @Override public GarageDto getGarageById(Long id) {
	return garageMapper.toDto(findGarageById(id));
    }

    @Override
    public Page<GarageDto> getAllGarages(Pageable pageable) {
	return garageRepository.findAll(pageable)
			.map(garageMapper::toDto);
    }

    @Override public int getNombreVehicules(Long garageId) {
	return garageRepository.findAll().size();
    }

    private Garage findGarageById(Long id){
	return garageRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("Garage non trouvé"));
    }
}
