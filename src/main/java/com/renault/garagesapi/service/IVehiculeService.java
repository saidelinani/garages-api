package com.renault.garagesapi.service;

import com.renault.garagesapi.entity.Vehicule;

import java.util.List;

public interface IVehiculeService {

    Vehicule addVehicule(Vehicule vehicule);
    Vehicule addVehiculeToGarage(Long garageId, Vehicule vehicule);
    Vehicule updateVehicule(Long id, Vehicule vehicule);
    void deleteVehicule(Long id);
    List<Vehicule> getVehiculesByGarage(Long garageId);
    List<Vehicule> getVehiculesByModele(String modele);
}
