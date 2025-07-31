package com.renault.garagesapi.dto;

import java.time.DayOfWeek;
import java.util.List;

public record JourHoraireDto(
		DayOfWeek jour,
		List<OpeningTimeDto> horaires
) {}