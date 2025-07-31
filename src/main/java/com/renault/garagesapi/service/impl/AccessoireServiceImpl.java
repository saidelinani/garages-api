package com.renault.garagesapi.service.impl;

import com.renault.garagesapi.dto.AccessoireDto;
import com.renault.garagesapi.entity.Accessoire;
import com.renault.garagesapi.entity.Garage;
import com.renault.garagesapi.entity.Vehicule;
import com.renault.garagesapi.exception.ResourceNotFoundException;
import com.renault.garagesapi.mapper.AccessoireMapper;
import com.renault.garagesapi.mapper.VehiculeMapper;
import com.renault.garagesapi.repository.AccessoireRepository;
import com.renault.garagesapi.service.IAccessoireService;
import com.renault.garagesapi.service.IVehiculeService;
import jakarta.transaction.Transactional;

import java.util.List;

public class AccessoireServiceImpl implements IAccessoireService {

    private final AccessoireRepository accessoireRepository;
    private final AccessoireMapper accessoireMapper;
    private final IVehiculeService vehiculeService;
    private final VehiculeMapper vehiculeMapper;

    public AccessoireServiceImpl(AccessoireRepository accessoireRepository, AccessoireMapper accessoireMapper,
		    IVehiculeService vehiculeService, VehiculeMapper vehiculeMapper) {
	this.accessoireRepository = accessoireRepository;
	this.accessoireMapper = accessoireMapper;
	this.vehiculeService = vehiculeService;
	this.vehiculeMapper = vehiculeMapper;
    }

    @Override public AccessoireDto addAccessoire(AccessoireDto accessoireDto) {

	Accessoire accessoire = accessoireMapper.toEntity(accessoireDto);
	Accessoire savedAccessoire = accessoireRepository.save(accessoire);
	return accessoireMapper.toDto(savedAccessoire);
    }

    @Override
    @Transactional
    public AccessoireDto addAccessoireToVehicule(Long vehiculeId, AccessoireDto accessoireDto) {

	Vehicule vehicule = vehiculeMapper.toEntity(vehiculeService.getVehiculeById(vehiculeId));

	Accessoire accessoire = accessoireMapper.toEntity(accessoireDto);
	accessoire.setVehicule(vehicule);

	Accessoire savedAccessoire = accessoireRepository.save(accessoire);

	return accessoireMapper.toDto(savedAccessoire);
    }

    @Override
    public AccessoireDto updateAccessoire(Long id, AccessoireDto accessoireDto) {

	Accessoire existingAccessoire = findAccessoireById(id);

	Accessoire accessoire = accessoireMapper.toEntity(accessoireDto);
	accessoire.setId(id);
	accessoire.setVehicule(existingAccessoire.getVehicule());

	return accessoireMapper.toDto(accessoireRepository.save(accessoire));
    }

    @Override
    @Transactional
    public void deleteAccessoire(Long id) {
	Accessoire accessoire = findAccessoireById(id);

	accessoireRepository.delete(accessoire);
    }

    @Override public List<AccessoireDto> getAccessoiresByVehicule(Long vehiculeId) {
	return List.of();
    }

    private Accessoire findAccessoireById(Long id){
	return accessoireRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("Accessoire non trouvé"));
    }
}
