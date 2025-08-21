package com.renault.garagesapi.mappers;

import com.renault.garagesapi.dtos.GarageDto;
import com.renault.garagesapi.entities.Garage;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring", uses = {DayScheduleMapper.class})
public interface GarageMapper {

    public Garage toEntity(GarageDto garageDto);
    public GarageDto toDto(Garage garage);
    List<GarageDto> toDtoList(List<Garage> garages);
}
