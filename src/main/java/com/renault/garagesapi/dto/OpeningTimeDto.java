package com.renault.garagesapi.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

public record OpeningTimeDto(

		@NotNull(message = "L'heure de début est obligatoire")
		LocalTime startTime,

		@NotNull(message = "L'heure de fin est obligatoire")
		LocalTime endTime
) {
	@AssertTrue(message = "L'heure de début doit être avant l'heure de fin")
	public boolean isValidPeriod() {
		return startTime != null && endTime != null && startTime.isBefore(endTime);
	}
}
