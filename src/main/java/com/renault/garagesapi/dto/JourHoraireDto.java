package com.renault.garagesapi.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.util.List;

public record JourHoraireDto(

		@NotNull(message = "Le jour est obligatoire")
		DayOfWeek jour,

		// TOdo
		@NotEmpty(message = "La liste des creneaux ne doit pas être vide")
		List<@Valid OpeningTimeDto> creneaux
) {}