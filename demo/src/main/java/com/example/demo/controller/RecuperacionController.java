package com.example.demo.controller;

import com.example.demo.model.entities.TokenRecuperacion;
import com.example.demo.model.entities.Usuario;
import com.example.demo.model.service.TokenRecuperacionService;
import com.example.demo.model.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import com.example.demo.model.dto.CorreoDTO;
import com.example.demo.model.entities.TokenRecuperacion;
import com.example.demo.model.entities.Usuario;
import com.example.demo.model.service.TokenRecuperacionService;
import com.example.demo.model.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/recuperar")
public class RecuperacionController {

    private final UsuarioService usuarioService;
    private final TokenRecuperacionService tokenService;

    public RecuperacionController(UsuarioService usuarioService, TokenRecuperacionService tokenService) {
        this.usuarioService = usuarioService;
        this.tokenService = tokenService;
    }

    @PostMapping("/solicitar")
    public ResponseEntity<?> solicitarRecuperacion(@RequestBody CorreoDTO request) {
        Optional<Usuario> usuarioOpt = usuarioService.findByCorreo(request.getCorreo());
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe usuario con ese correo");
        }

        Usuario usuario = usuarioOpt.get();
        TokenRecuperacion token = new TokenRecuperacion();
        token.setUsuario(usuario);
        token.setToken(UUID.randomUUID().toString());
        token.setExpiraEn(LocalDateTime.now().plusMinutes(30));
        tokenService.save(token);

        System.out.println("Token de recuperación: " + token.getToken());
        return ResponseEntity.ok("Token generado y guardado correctamente");
    }

    @PostMapping("/cambiar")
    public ResponseEntity<?> cambiarClave(@RequestParam String token, @RequestParam String nuevaClave) {
        Optional<TokenRecuperacion> tokenOpt = tokenService.findAll().stream()
                .filter(t -> t.getToken().equals(token))
                .findFirst();

        if (tokenOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Token inválido");
        }

        TokenRecuperacion tokenRecuperacion = tokenOpt.get();

        if (tokenRecuperacion.isUsado() || tokenRecuperacion.getExpiraEn().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token expirado o ya usado");
        }

        Usuario usuario = tokenRecuperacion.getUsuario();
        usuarioService.actualizarClave(usuario, nuevaClave);

        tokenRecuperacion.setUsado(true);
        tokenService.save(tokenRecuperacion);

        return ResponseEntity.ok("Contraseña actualizada correctamente");
    }
}
