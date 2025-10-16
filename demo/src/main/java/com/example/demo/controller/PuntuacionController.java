package com.example.demo.controller;

import com.example.demo.model.entities.Puntuacion;
import com.example.demo.model.service.PuntuacionService;
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
@RequestMapping("/api/puntuaciones")
public class PuntuacionController {

    private final PuntuacionService puntuacionService;

    public PuntuacionController(PuntuacionService puntuacionService) {
        this.puntuacionService = puntuacionService;
    }

    @GetMapping
    public List<Puntuacion> getAll() {
        return puntuacionService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Puntuacion> getById(@PathVariable Long id) {
        return puntuacionService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Puntuacion> create(@RequestBody Puntuacion puntuacion) {
        Puntuacion saved = puntuacionService.save(puntuacion);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Puntuacion> update(@PathVariable Long id, @RequestBody Puntuacion puntuacion) {
        return puntuacionService.findById(id)
                .map(existing -> {
                    puntuacion.setId(existing.getId());
                    Puntuacion updated = puntuacionService.save(puntuacion);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        return puntuacionService.findById(id)
                .map(existing -> {
                    puntuacionService.deleteById(id);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
