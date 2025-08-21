package com.renault.garagesapi.repositories;

import com.renault.garagesapi.entities.Garage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GarageRepository extends JpaRepository<Garage, Long> {

    @Query("SELECT COUNT(v) FROM Vehicle v WHERE v.garage.id = :garageId")
    int countByGarageId(Long garageId);
}
