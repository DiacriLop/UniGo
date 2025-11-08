package com.example.demo.controller;

import com.example.demo.model.entities.TokenRecuperacion;
import com.example.demo.model.service.TokenRecuperacionService;
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
@RequestMapping("/api/tokens-recuperacion")
public class TokenRecuperacionController {

    private final TokenRecuperacionService tokenRecuperacionService;

    public TokenRecuperacionController(TokenRecuperacionService tokenRecuperacionService) {
        this.tokenRecuperacionService = tokenRecuperacionService;
    }

    @GetMapping
    public List<TokenRecuperacion> getAll() {
        return tokenRecuperacionService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TokenRecuperacion> getById(@PathVariable Long id) {
        return tokenRecuperacionService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TokenRecuperacion> create(@RequestBody TokenRecuperacion tokenRecuperacion) {
        TokenRecuperacion saved = tokenRecuperacionService.save(tokenRecuperacion);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TokenRecuperacion> update(@PathVariable Long id, @RequestBody TokenRecuperacion tokenRecuperacion) {
        return tokenRecuperacionService.findById(id)
                .map(existing -> {
                    tokenRecuperacion.setId(existing.getId());
                    TokenRecuperacion updated = tokenRecuperacionService.save(tokenRecuperacion);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        return tokenRecuperacionService.findById(id)
                .map(existing -> {
                    tokenRecuperacionService.deleteById(id);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
