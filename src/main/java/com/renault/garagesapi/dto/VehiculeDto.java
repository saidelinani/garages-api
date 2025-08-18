package com.renault.garagesapi.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.renault.garagesapi.enums.TypeCarburant;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
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
		TypeCarburant typeCarburant,

		@JsonSetter(nulls = Nulls.AS_EMPTY)
		List<@Valid AccessoireDto> accessoires
) {
	@AssertTrue(message = "L'année de fabrication ne peut pas être dans le futur")
	public boolean isValidYear() {
		return anneeFabrication != null
				&& !anneeFabrication.isAfter(Year.now());
	}
}
