package com.renault.garagesapi.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.util.List;

public record DayScheduleDto(

		@NotNull(message = "Day is required")
		DayOfWeek dayOfWeek,

		@NotEmpty(message = "The list of opening times must not be empty")
		List<@Valid OpeningTimeDto> openingTimes
) {}