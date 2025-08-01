package com.renault.garagesapi.controller;

import com.renault.garagesapi.dto.AccessoireDto;
import com.renault.garagesapi.dto.VehiculeDto;
import com.renault.garagesapi.repository.VehiculeRepository;
import com.renault.garagesapi.service.IVehiculeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/vehicules")
public class VehiculeController {

    private final IVehiculeService vehiculeService;

    public VehiculeController(IVehiculeService vehiculeService) {
        this.vehiculeService = vehiculeService;
    }

    public ResponseEntity<VehiculeDto> addVehicule(VehiculeDto vehiculeDto) {
        return null;
    }

    @GetMapping("/{idVehicule}")
    public ResponseEntity<VehiculeDto> getVehiculeById(@PathVariable Long idVehicule) {
        return ResponseEntity.ok(vehiculeService.getVehiculeById(idVehicule));
    }
}
