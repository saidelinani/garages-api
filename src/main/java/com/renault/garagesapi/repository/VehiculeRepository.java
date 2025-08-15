package com.renault.garagesapi.repository;

import com.renault.garagesapi.entity.Vehicule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@RepositoryRestResource
public interface VehiculeRepository extends JpaRepository<Vehicule, Long> {
    public List<Vehicule> findByGarageId(Long idVehicule);
    public List<Vehicule> findByBrand(String brand);
}