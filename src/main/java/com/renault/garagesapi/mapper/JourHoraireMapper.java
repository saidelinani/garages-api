package com.renault.garagesapi.mapper;

import com.renault.garagesapi.dto.JourHoraireDto;
import com.renault.garagesapi.entity.JourHoraire;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface JourHoraireMapper {

    JourHoraireDto toDto(JourHoraire jourHoraire);
    JourHoraire toEntity(JourHoraireDto jourHoraireDto);
    List<JourHoraireDto> toDtoList(List<JourHoraire> jours);
}