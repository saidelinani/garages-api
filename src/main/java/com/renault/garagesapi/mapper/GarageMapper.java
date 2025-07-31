package com.renault.garagesapi.mapper;

import com.renault.garagesapi.dto.GarageDto;
import com.renault.garagesapi.entity.Garage;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface GarageMapper {

    public Garage toEntity(GarageDto garageDto);
    public GarageDto toDto(Garage garage);
    List<GarageDto> toDtoList(List<Garage> garages);
}
