package com.renault.garagesapi.repositories;

import com.renault.garagesapi.entities.Accessory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AccessoryRepository extends JpaRepository<Accessory, Long> {

    List<Accessory> findByVehicleId(Long vehiculeId);
}
