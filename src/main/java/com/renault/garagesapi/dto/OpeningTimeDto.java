package com.renault.garagesapi.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

public record OpeningTimeDto(

		@NotNull(message = "L'heure de début est obligatoire")
		LocalTime startTime,

		@NotNull(message = "L'heure de fin est obligatoire")
		LocalTime endTime
) {}
