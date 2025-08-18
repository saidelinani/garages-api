package com.renault.garagesapi.repository;

import com.renault.garagesapi.entity.Garage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GarageRepository extends JpaRepository<Garage, Long> {

    @Query("SELECT COUNT(v) FROM Vehicule v WHERE v.garage.id = :garageId")
    int countByGarageId(Long garageId);
}
