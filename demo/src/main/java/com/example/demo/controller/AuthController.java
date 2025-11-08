package com.example.demo.controller;

import com.example.demo.model.entities.Usuario;
import com.example.demo.model.service.UsuarioService;
import com.example.demo.security.CustomUserDetails;
import com.example.demo.security.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping(value = "/api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

    private final UsuarioService usuarioService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UsuarioService usuarioService,
                          AuthenticationManager authenticationManager,
                          JwtTokenProvider tokenProvider,
                          PasswordEncoder passwordEncoder) {
        this.usuarioService = usuarioService;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> authenticateUser(@RequestBody Map<String, String> loginRequest) {
        Map<String, Object> response = new HashMap<>();
        
        // Log the incoming request for debugging
        System.out.println("Login request: " + loginRequest);

        String correo = loginRequest.get("correo");
        String contrasena = loginRequest.get("contrasena");

        // Validate required fields
        if (correo == null || correo.trim().isEmpty() || contrasena == null || contrasena.trim().isEmpty()) {
            return createErrorResponse("Correo y contraseña son requeridos");
        }

        try {
            // Find user by email
            Optional<Usuario> usuarioOpt = usuarioService.findByCorreo(correo);
            if (usuarioOpt.isEmpty()) {
                return createErrorResponse("Correo o contraseña incorrectos", HttpStatus.UNAUTHORIZED);
            }

            Usuario usuario = usuarioOpt.get();
            
            // Debug log
            System.out.println("Verifying password for user: " + usuario.getCorreo());
            System.out.println("Stored hash: " + usuario.getClaveHash());
            
            // Verify password
            boolean passwordMatches = usuarioService.verificarClave(usuario, contrasena);
            System.out.println("Password matches: " + passwordMatches);
            
            if (!passwordMatches) {
                return createErrorResponse("Correo o contraseña incorrectos", HttpStatus.UNAUTHORIZED);
            }

            // Create authentication object
            CustomUserDetails userDetails = new CustomUserDetails(usuario);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generate JWT token
            String jwt = tokenProvider.generateToken(authentication);

            // Prepare response
            Map<String, Object> loginResponse = new HashMap<>();
            loginResponse.put("token", jwt);
            loginResponse.put("tipo", "Bearer");
            loginResponse.put("id", userDetails.getId());
            loginResponse.put("correo", userDetails.getUsername());
            loginResponse.put("nombreCompleto", userDetails.getFullName());

            response.put("ok", true);
            response.put("mensaje", "Inicio de sesión exitoso");
            response.put("data", loginResponse);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return createErrorResponse("Error al iniciar sesión: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/registro", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registrarUsuario(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Validate required fields
            String nombre = request.get("nombre");
            String apellido = request.get("apellido");
            String correo = request.get("correo");
            String contrasena = request.get("contrasena");
            String fechaNacimientoStr = request.get("fechaNacimiento");

            // Debug log
            System.out.println("Registration request - nombre: " + nombre + ", correo: " + correo);

            // Basic validations
            if (nombre == null || nombre.trim().isEmpty()) {
                return createErrorResponse("El nombre es requerido");
            }

            if (apellido == null || apellido.trim().isEmpty()) {
                return createErrorResponse("El apellido es requerido");
            }

            if (correo == null || correo.trim().isEmpty() || !correo.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                return createErrorResponse("El correo electrónico no es válido");
            }

            // Check if email already exists
            if (usuarioService.findByCorreo(correo).isPresent()) {
                return createErrorResponse("El correo electrónico ya está registrado", HttpStatus.CONFLICT);
            }

            if (contrasena == null || contrasena.trim().isEmpty() || contrasena.length() < 8) {
                return createErrorResponse("La contraseña debe tener al menos 8 caracteres");
            }
            // Create new user
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setNombres(nombre.trim());
            nuevoUsuario.setApellidos(apellido.trim());
            nuevoUsuario.setCorreo(correo.trim().toLowerCase());
            
            // Set default role (cliente)
            nuevoUsuario.setRol(Usuario.Rol.cliente);
            
            // Debug log before hashing
            System.out.println("Raw password before hashing: " + contrasena);
            
            // Set the password (it will be hashed in the service)
            nuevoUsuario.setClaveHash(contrasena);
            
            // Debug log after hashing
            System.out.println("Hashed password: " + nuevoUsuario.getClaveHash());
            System.out.println("User role set to: " + nuevoUsuario.getRol());
            
            // Set birth date if provided
            if (fechaNacimientoStr != null && !fechaNacimientoStr.trim().isEmpty()) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date fechaNacimiento = sdf.parse(fechaNacimientoStr);
                    nuevoUsuario.setFechaNacimiento(fechaNacimiento);
                } catch (Exception e) {
                    return createErrorResponse("Formato de fecha inválido. Use YYYY-MM-DD");
                }
            }

            // Save user
            Usuario usuarioGuardado = usuarioService.save(nuevoUsuario);
            usuarioGuardado.setClaveHash(null); // Don't return password hash

            // Build response
            response.put("ok", true);
            response.put("mensaje", "Usuario registrado exitosamente");
            response.put("data", usuarioGuardado);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            return createErrorResponse("Error al procesar la solicitud: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/usuario-actual")
    public ResponseEntity<?> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        Map<String, Object> response = new HashMap<>();
        response.put("ok", true);
        response.put("data", Map.of(
                "id", userDetails.getId(),
                "correo", userDetails.getUsername(),
                "nombreCompleto", userDetails.getFullName()
        ));

        return ResponseEntity.ok(response);
    }

    // Helper methods for error responses
    private ResponseEntity<Map<String, Object>> createErrorResponse(String message) {
        return createErrorResponse(message, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Map<String, Object>> createErrorResponse(String message, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("ok", false);
        response.put("mensaje", message);
        return new ResponseEntity<>(response, status);
    }
}

