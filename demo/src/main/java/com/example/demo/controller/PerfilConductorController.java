package com.example.demo.controller;

import com.example.demo.model.entities.PerfilConductor;
import com.example.demo.model.service.PerfilConductorService;
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
@RequestMapping("/api/perfiles-conductor")
public class PerfilConductorController {

    private final PerfilConductorService perfilConductorService;

    public PerfilConductorController(PerfilConductorService perfilConductorService) {
        this.perfilConductorService = perfilConductorService;
    }

    @GetMapping
    public List<PerfilConductor> getAll() {
        return perfilConductorService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PerfilConductor> getById(@PathVariable Long id) {
        return perfilConductorService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PerfilConductor> create(@RequestBody PerfilConductor perfilConductor) {
        PerfilConductor saved = perfilConductorService.save(perfilConductor);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PerfilConductor> update(@PathVariable Long id, @RequestBody PerfilConductor perfilConductor) {
        return perfilConductorService.findById(id)
                .map(existing -> {
                    perfilConductor.setUsuarioId(existing.getUsuarioId());
                    PerfilConductor updated = perfilConductorService.save(perfilConductor);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        return perfilConductorService.findById(id)
                .map(existing -> {
                    perfilConductorService.deleteById(id);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
