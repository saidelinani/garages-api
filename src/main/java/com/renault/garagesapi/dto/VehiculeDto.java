package com.renault.garagesapi.dto;

import java.time.Year;
import java.util.List;

public record VehiculeDto(
		Long id,
		String brand,
		Year anneeFabrication,
		String typeCarburant,
		List<AccessoireDto> accessoires
) {}
