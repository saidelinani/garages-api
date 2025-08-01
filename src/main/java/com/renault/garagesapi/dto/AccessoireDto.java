package com.renault.garagesapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record AccessoireDto(
		Long id,

		@NotBlank(message = "Le nom est obligatoire")
		String nom,

		@NotBlank(message = "La description est obligatoire")
		String description,

		@Positive(message = "Le prix doit être strictement positif")
		double prix,

		@NotBlank(message = "Le type est obligatoire")
		String type
) {}