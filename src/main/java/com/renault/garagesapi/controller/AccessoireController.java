package com.renault.garagesapi.controller;

import com.renault.garagesapi.dto.AccessoireDto;
import com.renault.garagesapi.service.IAccessoireService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


    @DeleteMapping("/{id}")
    public void deleteAccessoire(@PathVariable Long id) {
        accessoireService.deleteAccessoire(id);
    }


}
