package com.renault.garagesapi.mapper;

import com.renault.garagesapi.dto.AccessoireDto;
import com.renault.garagesapi.entity.Accessoire;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface AccessoireMapper {

    AccessoireDto toDto(Accessoire accessoire);
    Accessoire toEntity(AccessoireDto accessoireDto);
    List<AccessoireDto> toDtoList(List<Accessoire> accessoires);
}
