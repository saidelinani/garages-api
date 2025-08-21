package com.renault.garagesapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record AccessoryDto(

		Long id,

		@NotBlank(message = "Name is required")
		String name,

		@NotBlank(message = "Description Name is required")
		String description,

		@Positive(message = "Price must be greater than 0")
		double price,

		@NotBlank(message = "Type is required")
		String type
) {}