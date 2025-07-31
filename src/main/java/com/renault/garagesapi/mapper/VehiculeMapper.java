package com.renault.garagesapi.mapper;

import com.renault.garagesapi.dto.VehiculeDto;
import com.renault.garagesapi.entity.Vehicule;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface VehiculeMapper {
    public Vehicule toEntity(VehiculeDto garageDto);
    public VehiculeDto toDto(Vehicule vehicule);
    public List<VehiculeDto> toDtoList(List<Vehicule> vehicule);
}
