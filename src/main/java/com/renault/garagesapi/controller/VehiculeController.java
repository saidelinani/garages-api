package com.renault.garagesapi.controller;

import com.renault.garagesapi.dto.VehiculeDto;
import com.renault.garagesapi.service.IVehiculeService;
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
public class VehiculeController {

    private final IVehiculeService vehiculeService;

    public VehiculeController(IVehiculeService vehiculeService) {
        this.vehiculeService = vehiculeService;
    }

    @PostMapping
    public ResponseEntity<VehiculeDto> addVehicule(@Valid @RequestBody VehiculeDto vehiculeDto) {
        VehiculeDto savedVehicule = vehiculeService.addVehicule(vehiculeDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedVehicule);
    }

    @PostMapping("/garage/{garageId}")
    public ResponseEntity<VehiculeDto> addVehiculeToGarage(@PathVariable Long garageId, @RequestBody VehiculeDto vehiculeDto){
        VehiculeDto savedVehicule = vehiculeService.addVehiculeToGarage(garageId, vehiculeDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedVehicule);
    }

    @GetMapping("/{idVehicule}")
    public ResponseEntity<VehiculeDto> getVehiculeById(@PathVariable Long idVehicule) {
        return ResponseEntity.ok(vehiculeService.getVehiculeById(idVehicule));
    }

    @PutMapping("/{idVehicule}")
    public ResponseEntity<VehiculeDto> updateVehicule(@PathVariable Long idVehicule, @Valid @RequestBody VehiculeDto vehiculeDto) {
        VehiculeDto savedVehicule = vehiculeService.updateVehicule(idVehicule, vehiculeDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedVehicule);
    }

    @DeleteMapping("/{idVehicule}")
    public ResponseEntity<Void> deleteVehicule(@PathVariable Long idVehicule) {
        vehiculeService.deleteVehicule(idVehicule);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/garage/{idGarage}")
    public ResponseEntity<List<VehiculeDto>> getVehiculesByGarage(@PathVariable Long idGarage) {

        List<VehiculeDto> vehicules = vehiculeService.getVehiculesByGarage(idGarage);
        return ResponseEntity.ok(vehicules);
    }
}
