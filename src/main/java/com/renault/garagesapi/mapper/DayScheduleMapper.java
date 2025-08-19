package com.renault.garagesapi.mapper;

import com.renault.garagesapi.dto.DayScheduleDto;
import com.renault.garagesapi.entity.DaySchedule;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring", uses = OpeningTimeMapper.class)
public interface DayScheduleMapper {

    DayScheduleDto toDto(DaySchedule daySchedule);
    DaySchedule toEntity(DayScheduleDto dayScheduleDto);
    List<DayScheduleDto> toDtoList(List<DaySchedule> jours);
}