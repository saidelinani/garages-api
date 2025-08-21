package com.renault.garagesapi.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.renault.garagesapi.enums.FuelType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Year;
import java.util.List;

public record VehicleDto(
		Long id,

		@NotBlank(message = "Brand is required")
		String brand,

		@NotNull(message = "Year of manufacture is required")
		Year yearOfManufacture,

		@NotNull(message = "Fuel type is required")
        FuelType fuelType,

		@JsonSetter(nulls = Nulls.AS_EMPTY)
		List<@Valid AccessoryDto> accessories
) {
	@AssertTrue(message = "Year of manufacture cannot be in the future")
	public boolean isValidYear() {
		return yearOfManufacture != null
				&& !yearOfManufacture.isAfter(Year.now());
	}
}
