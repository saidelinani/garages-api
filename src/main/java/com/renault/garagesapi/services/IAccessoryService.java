package com.renault.garagesapi.service;

import com.renault.garagesapi.dto.AccessoryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface IAccessoryService {

    AccessoryDto addAccessoire(AccessoryDto accessoire);
    AccessoryDto addAccessoireToVehicule(Long vehiculeId, AccessoryDto accessoryDto);
    AccessoryDto updateAccessoire(Long id, AccessoryDto accessoryDto);
    void deleteAccessoire(Long id);
    Page<AccessoryDto> getAllAccessoires(Pageable pageable);
    List<AccessoryDto> getAccessoiresByVehicule(Long vehiculeId);
    void attachAccessoireToVehicule(Long accessoireId, Long vehiculeId);
}
