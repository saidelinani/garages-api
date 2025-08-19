package com.renault.garagesapi.mapper;

import com.renault.garagesapi.dto.AccessoryDto;
import com.renault.garagesapi.entity.Accessory;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface AccessoryMapper {

    AccessoryDto toDto(Accessory accessory);
    Accessory toEntity(AccessoryDto accessoryDto);
    List<AccessoryDto> toDtoList(List<Accessory> accessories);
}
