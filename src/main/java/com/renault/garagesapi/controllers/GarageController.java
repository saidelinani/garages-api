package com.renault.garagesapi.controller;

import com.renault.garagesapi.dto.GarageDto;
import com.renault.garagesapi.service.IGarageService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/garages")
public class GarageController {

    private final IGarageService garageService;

    public GarageController(IGarageService garageService) {
        this.garageService = garageService;
    }

    @PostMapping
    public ResponseEntity<GarageDto> addGarage(@Valid @RequestBody GarageDto garageDto) {
        GarageDto savedGarage = garageService.addGarage(garageDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedGarage);
    }

    @PutMapping("/{idGarage}")
    public ResponseEntity<GarageDto> updateGarage(@PathVariable Long idGarage,
                                                  @Valid @RequestBody GarageDto garageDto) {
        return ResponseEntity.ok(garageService.updateGarage(idGarage, garageDto));
    }

    @GetMapping
    public ResponseEntity<Page<GarageDto>> getAllGarages(Pageable pageable) {
        Page<GarageDto> garages = garageService.getAllGarages(pageable);
        return ResponseEntity.ok(garages);
    }

    @GetMapping("/{idGarage}")
    public ResponseEntity<GarageDto> getGarageById(@PathVariable Long idGarage) {

        return ResponseEntity.ok(garageService.getGarageDtoById(idGarage));
    }

    @DeleteMapping("/{idGarage}")
    public ResponseEntity<Void> deleteGarage(@PathVariable Long idGarage) {
        garageService.deleteGarage(idGarage);
        return ResponseEntity.ok().build();
    }
}
