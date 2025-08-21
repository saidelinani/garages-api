package com.renault.garagesapi.mapper;

import com.renault.garagesapi.dto.VehicleDto;
import com.renault.garagesapi.entities.Vehicle;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface VehicleMapper {
    public Vehicle toEntity(VehicleDto garageDto);
    public VehicleDto toDto(Vehicle vehicle);
    public List<VehicleDto> toDtoList(List<Vehicle> vehicle);
}
