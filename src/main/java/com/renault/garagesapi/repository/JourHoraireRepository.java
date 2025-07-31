package com.renault.garagesapi.repository;

import com.renault.garagesapi.entity.JourHoraire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JourHoraireRepository extends JpaRepository<JourHoraire, Long> {
}
