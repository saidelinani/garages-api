package com.renault.garagesapi.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.List;

public record GarageDto(
		Long id,

		@NotBlank(message = "Le nom du garage est obligatoire")
		String name,

		@NotBlank(message = "La ville est obligatoire")
		String city,

		@NotBlank(message = "L'adresse est obligatoire")
		String address,

		@NotBlank(message = "Le téléphone est obligatoire")
		@Pattern(regexp = "\\d{10}", message = "Le téléphone doit contenir 10 chiffres")
		String telephone,

		@Email(message = "L'email doit être valide")
		@NotBlank(message = "L'email est obligatoire")
		String email,

		@NotNull(message = "Les horaires d'ouverture sont obligatoires")
		List<@Valid DayScheduleDto> horairesOuverture
) {}
