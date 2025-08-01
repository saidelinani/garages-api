package com.renault.garagesapi.controller;

import com.renault.garagesapi.dto.AccessoireDto;
import com.renault.garagesapi.service.IAccessoireService;
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
public class AccessoireController {

    private final IAccessoireService accessoireService;

    public AccessoireController(IAccessoireService accessoireService) {
        this.accessoireService = accessoireService;
    }

    @PostMapping
    public ResponseEntity<AccessoireDto> addAccessoire(@Valid @RequestBody AccessoireDto accessoireDto) {
        AccessoireDto savedAccessoire = accessoireService.addAccessoire(accessoireDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAccessoire);
    }

    @PutMapping("/{idAccessoire}")
    public ResponseEntity<AccessoireDto> updateAccessoire(@PathVariable Long idAccessoire, @Valid @RequestBody AccessoireDto accessoireDto) {
        AccessoireDto savedAccessoire = accessoireService.updateAccessoire(idAccessoire, accessoireDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAccessoire);
    }

    @DeleteMapping("/{idAccessoire}")
    public ResponseEntity<Void> deleteAccessoire(@PathVariable Long idAccessoire) {
        accessoireService.deleteAccessoire(idAccessoire);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Page<AccessoireDto>> getAllAccessoires(Pageable pageable) {

        Page<AccessoireDto> accessoires = accessoireService.getAllAccessoires(pageable);
        return ResponseEntity.ok(accessoires);
    }

    @GetMapping("/vehicule/{idVehicule}")
    public ResponseEntity<List<AccessoireDto>> getAccessoiresByVehicule(@PathVariable Long idVehicule) {

        List<AccessoireDto> accessoires = accessoireService.getAccessoiresByVehicule(idVehicule);
        return ResponseEntity.ok(accessoires);
    }

    @PostMapping("/vehicule/{vehiculeId}")
    public ResponseEntity<AccessoireDto> addAccessoireToVehicule(
                    @PathVariable Long vehiculeId,
                    @Valid @RequestBody AccessoireDto accessoireDto) {

        AccessoireDto saved = accessoireService.addAccessoireToVehicule(vehiculeId, accessoireDto);
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
