package com.renault.garagesapi.repository;

import com.renault.garagesapi.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    public List<Vehicle> findByGarageId(Long garageId);
    public List<Vehicle> findByBrand(String brand);
}