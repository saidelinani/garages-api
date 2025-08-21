package com.renault.garagesapi.controller;

import com.renault.garagesapi.dto.VehicleDto;
import com.renault.garagesapi.service.IVehicleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/vehicules")
public class VehicleController {

    private final IVehicleService vehiculeService;

    public VehicleController(IVehicleService vehiculeService) {
        this.vehiculeService = vehiculeService;
    }

    @PostMapping
    public ResponseEntity<VehicleDto> addVehicule(@Valid @RequestBody VehicleDto vehicleDto) {
        VehicleDto savedVehicule = vehiculeService.addVehicule(vehicleDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedVehicule);
    }

    @PostMapping("/garage/{garageId}")
    public ResponseEntity<VehicleDto> addVehiculeToGarage(@Valid @PathVariable Long garageId, @RequestBody VehicleDto vehicleDto){
        VehicleDto savedVehicule = vehiculeService.addVehiculeToGarage(garageId, vehicleDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedVehicule);
    }

    @GetMapping("/{idVehicule}")
    public ResponseEntity<VehicleDto> getVehiculeById(@PathVariable Long idVehicule) {
        return ResponseEntity.ok(vehiculeService.getVehiculeById(idVehicule));
    }

    @PutMapping("/{idVehicule}")
    public ResponseEntity<VehicleDto> updateVehicule(@PathVariable Long idVehicule, @Valid @RequestBody VehicleDto vehicleDto) {
        VehicleDto savedVehicule = vehiculeService.updateVehicule(idVehicule, vehicleDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedVehicule);
    }

    @DeleteMapping("/{idVehicule}")
    public ResponseEntity<Void> deleteVehicule(@PathVariable Long idVehicule) {
        vehiculeService.deleteVehicule(idVehicule);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/garage/{idGarage}")
    public ResponseEntity<List<VehicleDto>> getVehiculesByGarage(@PathVariable Long idGarage) {

        List<VehicleDto> vehicules = vehiculeService.getVehiculesByGarage(idGarage);
        return ResponseEntity.ok(vehicules);
    }
}
