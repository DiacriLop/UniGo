package com.example.demo.controller;

import com.example.demo.model.entities.Comentario;
import com.example.demo.model.service.ComentarioService;
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
@RequestMapping("/api/comentarios")
public class ComentarioController {

    private final ComentarioService comentarioService;

    public ComentarioController(ComentarioService comentarioService) {
        this.comentarioService = comentarioService;
    }

    @GetMapping
    public List<Comentario> getAll() {
        return comentarioService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comentario> getById(@PathVariable Long id) {
        return comentarioService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Comentario> create(@RequestBody Comentario comentario) {
        Comentario saved = comentarioService.save(comentario);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Comentario> update(@PathVariable Long id, @RequestBody Comentario comentario) {
        return comentarioService.findById(id)
                .map(existing -> {
                    comentario.setId(existing.getId());
                    Comentario updated = comentarioService.save(comentario);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        return comentarioService.findById(id)
                .map(existing -> {
                    comentarioService.deleteById(id);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
