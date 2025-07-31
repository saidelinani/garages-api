package com.renault.garagesapi.controller;

import com.renault.garagesapi.service.IAccessoireService;
import org.springframework.web.bind.annotation.DeleteMapping;

public class AccessoireController {

    private final IAccessoireService accessoireService;

    public AccessoireController(IAccessoireService accessoireService) {
	this.accessoireService = accessoireService;
    }

    @DeleteMapping()
    public void deleteAccessoire(Long id) {

    }
}
