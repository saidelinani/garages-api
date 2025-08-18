package com.renault.garagesapi.service.impl;

import com.renault.garagesapi.dto.AccessoireDto;
import com.renault.garagesapi.entity.Accessoire;
import com.renault.garagesapi.entity.Vehicule;
import com.renault.garagesapi.exception.AccessoryAlreadyAssignedException;
import com.renault.garagesapi.exception.ResourceNotFoundException;
import com.renault.garagesapi.mapper.AccessoireMapper;
import com.renault.garagesapi.mapper.VehiculeMapper;
import com.renault.garagesapi.repository.AccessoireRepository;
import com.renault.garagesapi.service.IAccessoireService;
import com.renault.garagesapi.service.IVehiculeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccessoireServiceImpl implements IAccessoireService {

    private final AccessoireRepository accessoireRepository;
    private final AccessoireMapper accessoireMapper;
    private final IVehiculeService vehiculeService;
    private final VehiculeMapper vehiculeMapper;

    public AccessoireServiceImpl(AccessoireRepository accessoireRepository,
                                 AccessoireMapper accessoireMapper,
                                 IVehiculeService vehiculeService,
                                 VehiculeMapper vehiculeMapper) {
        this.accessoireRepository = accessoireRepository;
        this.accessoireMapper = accessoireMapper;
        this.vehiculeService = vehiculeService;
        this.vehiculeMapper = vehiculeMapper;
    }

    @Override
    public AccessoireDto addAccessoire(AccessoireDto accessoireDto) {

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

    @Override
    public Page<AccessoireDto> getAllAccessoires(Pageable pageable) {
        return accessoireRepository.findAll(pageable)
                        .map(accessoireMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccessoireDto> getAccessoiresByVehicule(Long vehiculeId) {

        List<Accessoire> accessoires = accessoireRepository.findByVehiculeId(vehiculeId);

        return accessoires.stream()
                .map(accessoireMapper::toDto)
                .collect(Collectors.toList());
    }

    private Accessoire findAccessoireById(Long id) {
        return accessoireRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Accessoire non trouvé"));
    }

    @Transactional
    @Override public void attachAccessoireToVehicule(Long accessoireId, Long vehiculeId) {

        Vehicule vehicule = vehiculeMapper.toEntity(vehiculeService.getVehiculeById(vehiculeId));

        Accessoire accessoire = findAccessoireById(accessoireId);

        // Vérifier si l'accessoire n'est pas déjà associé à un véhicule
        if (accessoire.getVehicule() != null) {
            throw new AccessoryAlreadyAssignedException("L'accessoire est déjà associé au véhicule avec l'ID " +
                            accessoire.getVehicule().getId());
        }

        accessoire.setVehicule(vehicule);

        accessoireRepository.save(accessoire);
    }
}
