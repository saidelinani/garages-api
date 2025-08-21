package com.renault.garagesapi.mappers;

import com.renault.garagesapi.dtos.OpeningTimeDto;
import com.renault.garagesapi.entities.OpeningTime;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface OpeningTimeMapper {

    OpeningTimeDto toDto(OpeningTime openingTime);
    OpeningTime toEntity(OpeningTimeDto openingTimeDto);
    List<OpeningTimeDto> toDtoList(List<OpeningTime> times);
}