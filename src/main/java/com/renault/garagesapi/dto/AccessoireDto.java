package com.renault.garagesapi.dto;

import com.renault.garagesapi.entity.Garage;
import org.antlr.v4.runtime.misc.NotNull;

public record AccessoireDto(
		Long id,
		String nom,
		String description,
		double prix,
		String type
) {}