package com.renault.garagesapi.mappers;

import com.renault.garagesapi.dtos.DayScheduleDto;
import com.renault.garagesapi.entities.DaySchedule;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring", uses = OpeningTimeMapper.class)
public interface DayScheduleMapper {

    DayScheduleDto toDto(DaySchedule daySchedule);
    DaySchedule toEntity(DayScheduleDto dayScheduleDto);
    List<DayScheduleDto> toDtoList(List<DaySchedule> jours);
}