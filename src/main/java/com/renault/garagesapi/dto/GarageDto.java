package com.renault.garagesapi.dto;

import java.util.List;

public record GarageDto(
		Long id,
		String name,
		String address,
		String telephone,
		String email,
		List<JourHoraireDto> horairesOuverture
) {}
