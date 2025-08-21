package com.renault.garagesapi.mappers;

import com.renault.garagesapi.dtos.AccessoryDto;
import com.renault.garagesapi.entities.Accessory;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface AccessoryMapper {

    AccessoryDto toDto(Accessory accessory);
    Accessory toEntity(AccessoryDto accessoryDto);
    List<AccessoryDto> toDtoList(List<Accessory> accessories);
}
