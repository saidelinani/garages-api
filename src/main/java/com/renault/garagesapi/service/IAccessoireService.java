package com.renault.garagesapi.service;


import com.renault.garagesapi.dto.AccessoireDto;
import com.renault.garagesapi.entity.Accessoire;
import java.util.List;

public interface IAccessoireService {

    AccessoireDto addAccessoire(AccessoireDto accessoire);
    AccessoireDto addAccessoireToVehicule(Long vehiculeId, AccessoireDto accessoireDto);
    AccessoireDto updateAccessoire(Long id, AccessoireDto accessoireDto);
    void deleteAccessoire(Long id);
    List<AccessoireDto> getAccessoiresByVehicule(Long vehiculeId);
}
