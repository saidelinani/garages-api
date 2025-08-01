package com.renault.garagesapi.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Year;
import java.util.List;

public record VehiculeDto(
		Long id,

		@NotBlank(message = "La marque est obligatoire")
		String brand,

		@NotNull(message = "L'année de fabrication est obligatoire")
		Year anneeFabrication,

		@NotBlank(message = "Le type de carburant est obligatoire")
		String typeCarburant,

		@JsonSetter(nulls = Nulls.AS_EMPTY)
		List<@Valid AccessoireDto> accessoires
) {}
