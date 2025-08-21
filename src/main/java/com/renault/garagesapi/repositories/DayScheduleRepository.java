package com.renault.garagesapi.repositories;

import com.renault.garagesapi.entities.DaySchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DayScheduleRepository extends JpaRepository<DaySchedule, Long> {
}
