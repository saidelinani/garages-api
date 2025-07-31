package com.renault.garagesapi.service;


import com.renault.garagesapi.entity.Accessoire;
import java.util.List;

public interface IAccessoireService {

    Accessoire addAccessoire(Accessoire accessoire);
    Accessoire addAccessoireToVehicule(Long vehiculeId, Accessoire accessoire);
    Accessoire updateAccessoire(Long id, Accessoire accessoire);
    void deleteAccessoire(Long id);
    List<Accessoire> getAccessoiresByVehicule(Long vehiculeId);
}
