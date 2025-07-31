package com.renault.garagesapi.repository;

import com.renault.garagesapi.entity.Vehicule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource
public interface VehiculeRepository extends JpaRepository<Vehicule, Long> {
}
