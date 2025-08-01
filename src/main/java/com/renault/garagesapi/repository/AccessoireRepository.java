package com.renault.garagesapi.repository;

import com.renault.garagesapi.entity.Accessoire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AccessoireRepository extends JpaRepository<Accessoire, Long> {

    public List<Accessoire> findByVehiculeId(Long vehiculeId);
}
