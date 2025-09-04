package com.sit.SITPass.controller;

import com.sit.SITPass.DTO.ManagesDTO;
import com.sit.SITPass.model.Facility;
import com.sit.SITPass.model.Manages;
import com.sit.SITPass.service.ManagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/api/manages")
public class ManagesController {

    @Autowired
    private ManagesService managesService;

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<ManagesDTO> getManages(@PathVariable Long id) {
        System.out.println("Get Manages: " + id);
        if (id == null) {
            return ResponseEntity.badRequest().body(new ManagesDTO());
        }
        System.out.println("The facility with id: " + id);
        Optional<Manages> manages = managesService.getManagerByFacilityId(id);
        System.out.println(manages);
        if (manages.isPresent()) {
            ManagesDTO managesDTO = new ManagesDTO(manages.get());
            return ResponseEntity.ok(managesDTO);
        }
        else {
            return ResponseEntity.notFound().build();
        }

    }

}
