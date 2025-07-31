package com.renault.garagesapi.dto;

import java.time.LocalTime;

public record OpeningTimeDto(
		LocalTime startTime,
		LocalTime endTime
) {}
