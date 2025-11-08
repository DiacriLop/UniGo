package com.example.demo.controller;

import com.example.demo.model.entities.Pago;
import com.example.demo.model.service.PagoService;
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
@RequestMapping("/api/pagos")
public class PagoController {

    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @GetMapping
    public List<Pago> getAll() {
        return pagoService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pago> getById(@PathVariable Long id) {
        return pagoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Pago> create(@RequestBody Pago pago) {
        Pago saved = pagoService.save(pago);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pago> update(@PathVariable Long id, @RequestBody Pago pago) {
        return pagoService.findById(id)
                .map(existing -> {
                    pago.setId(existing.getId());
                    Pago updated = pagoService.save(pago);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        return pagoService.findById(id)
                .map(existing -> {
                    pagoService.deleteById(id);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
