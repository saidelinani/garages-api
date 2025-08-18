package com.renault.garagesapi.service;

import com.renault.garagesapi.dto.AccessoireDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface IAccessoireService {

    AccessoireDto addAccessoire(AccessoireDto accessoire);
    AccessoireDto addAccessoireToVehicule(Long vehiculeId, AccessoireDto accessoireDto);
    AccessoireDto updateAccessoire(Long id, AccessoireDto accessoireDto);
    void deleteAccessoire(Long id);
    Page<AccessoireDto> getAllAccessoires(Pageable pageable);
    List<AccessoireDto> getAccessoiresByVehicule(Long vehiculeId);
    void attachAccessoireToVehicule(Long accessoireId, Long vehiculeId);
}
