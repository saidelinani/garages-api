package com.renault.garagesapi.dtos;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

public record OpeningTimeDto(

		@NotNull(message = "Start time is required")
		LocalTime startTime,

		@NotNull(message = "End time is required")
		LocalTime endTime
) {
	@AssertTrue(message = "Start time must be before end time")
	public boolean isValidPeriod() {
		return startTime != null && endTime != null && startTime.isBefore(endTime);
	}
}
