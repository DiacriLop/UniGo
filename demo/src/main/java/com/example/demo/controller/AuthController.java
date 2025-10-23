package com.example.demo.controller;

import com.example.demo.model.entities.Usuario;
import com.example.demo.model.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String correo = request.get("correo");
        String clave = request.get("clave");

        Optional<Usuario> usuarioOpt = usuarioService.findByCorreo(correo);
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Correo o contraseña incorrectos");
        }

        Usuario usuario = usuarioOpt.get();
        if (!usuarioService.verificarClave(usuario, clave)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Correo o contraseña incorrectos");
        }

        return ResponseEntity.ok(usuario); // Puedes devolver solo info básica si prefieres
    }
}

