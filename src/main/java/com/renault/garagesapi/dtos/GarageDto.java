package com.renault.garagesapi.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.List;

public record GarageDto(
		Long id,

		@NotBlank(message = "Name is required")
		String name,

		@NotBlank(message = "City is required")
		String city,

		@NotBlank(message = "Address is required")
		String address,

		@NotBlank(message = "Phone number is required")
		@Pattern(regexp = "\\d{10}", message = "Phone number must contain 10 digits")
		String phoneNumber,

		@Email(message = "Email must be valid")
		@NotBlank(message = "Email is required")
		String email,

		@NotNull(message = "Opening times are required")
		List<@Valid DayScheduleDto> daySchedules
) {}
