package com.renault.garagesapi.controller;

import com.renault.garagesapi.dto.AccessoryDto;
import com.renault.garagesapi.service.IAccessoryService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
@RequestMapping("/api/accessoires")
public class AccessoryController {

    private final IAccessoryService accessoireService;

    public AccessoryController(IAccessoryService accessoireService) {
        this.accessoireService = accessoireService;
    }

    @PostMapping
    public ResponseEntity<AccessoryDto> addAccessoire(@Valid @RequestBody AccessoryDto accessoryDto) {
        AccessoryDto savedAccessoire = accessoireService.addAccessoire(accessoryDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAccessoire);
    }

    @PutMapping("/{idAccessoire}")
    public ResponseEntity<AccessoryDto> updateAccessoire(@PathVariable Long idAccessoire, @Valid @RequestBody AccessoryDto accessoryDto) {
        AccessoryDto savedAccessoire = accessoireService.updateAccessoire(idAccessoire, accessoryDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAccessoire);
    }

    @DeleteMapping("/{idAccessoire}")
    public ResponseEntity<Void> deleteAccessoire(@PathVariable Long idAccessoire) {
        accessoireService.deleteAccessoire(idAccessoire);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Page<AccessoryDto>> getAllAccessoires(Pageable pageable) {

        Page<AccessoryDto> accessoires = accessoireService.getAllAccessoires(pageable);
        return ResponseEntity.ok(accessoires);
    }

    @GetMapping("/vehicule/{idVehicule}")
    public ResponseEntity<List<AccessoryDto>> getAccessoiresByVehicule(@PathVariable Long idVehicule) {

        List<AccessoryDto> accessoires = accessoireService.getAccessoiresByVehicule(idVehicule);
        return ResponseEntity.ok(accessoires);
    }

    @PostMapping("/vehicule/{vehiculeId}")
    public ResponseEntity<AccessoryDto> addAccessoireToVehicule(
                    @PathVariable Long vehiculeId,
                    @Valid @RequestBody AccessoryDto accessoryDto) {

        AccessoryDto saved = accessoireService.addAccessoireToVehicule(vehiculeId, accessoryDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PostMapping("/vehicule/{vehiculeId}/accessoire/{accessoireId}")
    public ResponseEntity<Void> attachAccessoireToVehicule(
                    @PathVariable Long vehiculeId,
                    @PathVariable Long accessoireId) {

        accessoireService.attachAccessoireToVehicule(accessoireId, vehiculeId);
        return ResponseEntity.ok().build();
    }
}
