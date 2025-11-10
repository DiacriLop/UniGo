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
import com.example.demo.security.AESPasswordEncoder;
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
    private final AESPasswordEncoder passwordEncoder;

    public AuthController(UsuarioService usuarioService,
                          AuthenticationManager authenticationManager,
                          JwtTokenProvider tokenProvider,
                          AESPasswordEncoder passwordEncoder) {
        this.usuarioService = usuarioService;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> authenticateUser(@RequestBody Map<String, String> loginRequest) {
        try {
            // Validar campos requeridos
            if (loginRequest == null || loginRequest.isEmpty()) {
                return createErrorResponse("Se requiere un objeto JSON con correo y contraseña");
            }

            String correo = loginRequest.get("correo");
            String contrasena = loginRequest.get("contrasena");

            // Validar que los campos no estén vacíos
            if (correo == null || correo.trim().isEmpty() || contrasena == null || contrasena.trim().isEmpty()) {
                return createErrorResponse("Correo y contraseña son requeridos");
            }

            // Buscar usuario por correo
            Usuario usuario = usuarioService.findByCorreo(correo.trim().toLowerCase())
                .orElseThrow(() -> new SecurityException("Credenciales inválidas"));
            
            // Verificar contraseña
            if (!usuarioService.verificarClave(usuario, contrasena)) {
                return createErrorResponse("Correo o contraseña incorrectos", HttpStatus.UNAUTHORIZED);
            }
            
            // Crear autenticación y generar token JWT
            CustomUserDetails userDetails = new CustomUserDetails(usuario);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
            String token = tokenProvider.generateToken(authentication);
            
            // Construir respuesta
            Map<String, Object> response = new HashMap<>();
            response.put("ok", true);
            response.put("mensaje", "Inicio de sesión exitoso");
            response.put("data", Map.of(
                "token", token,
                "usuario", Map.of(
                    "id", usuario.getId(),
                    "correo", usuario.getCorreo(),
                    "nombres", usuario.getNombres(),
                    "apellidos", usuario.getApellidos(),
                    "rol", usuario.getRol().name()
                )
            ));

            return ResponseEntity.ok(response);

        } catch (SecurityException e) {
            return createErrorResponse("Credenciales inválidas", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return createErrorResponse("Error al procesar la solicitud", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/registro", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> registrarUsuario(@RequestBody Map<String, String> request) {
        try {
            // Validar que la solicitud no esté vacía
            if (request == null || request.isEmpty()) {
                return createErrorResponse("Se requiere un objeto JSON con los datos del usuario");
            }

            // Validar campos requeridos
            String nombre = request.get("nombre");
            String apellido = request.get("apellido");
            String correo = request.get("correo");
            String contrasena = request.get("contrasena");
            String fechaNacimientoStr = request.get("fechaNacimiento");

            // Validaciones básicas
            if (nombre == null || nombre.trim().isEmpty()) {
                return createErrorResponse("El nombre es requerido");
            }

            if (apellido == null || apellido.trim().isEmpty()) {
                return createErrorResponse("El apellido es requerido");
            }

            if (correo == null || correo.trim().isEmpty() || !correo.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                return createErrorResponse("El correo electrónico no es válido");
            }

            // Verificar si el correo ya existe
            if (usuarioService.findByCorreo(correo.trim().toLowerCase()).isPresent()) {
                return createErrorResponse("El correo electrónico ya está registrado", HttpStatus.CONFLICT);
            }

            if (contrasena == null || contrasena.trim().isEmpty() || contrasena.length() < 8) {
                return createErrorResponse("La contraseña debe tener al menos 8 caracteres");
            }

            // Crear nuevo usuario
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setNombres(nombre.trim());
            nuevoUsuario.setApellidos(apellido.trim());
            nuevoUsuario.setCorreo(correo.trim().toLowerCase());
            nuevoUsuario.setRol(Usuario.Rol.cliente);
            
            // Establecer la contraseña (se encriptará en el servicio)
            nuevoUsuario.setClaveHash(contrasena);
            
            // Establecer fecha de nacimiento si se proporciona
            if (fechaNacimientoStr != null && !fechaNacimientoStr.trim().isEmpty()) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    sdf.setLenient(false);
                    Date fechaNacimiento = sdf.parse(fechaNacimientoStr.trim());
                    nuevoUsuario.setFechaNacimiento(fechaNacimiento);
                } catch (Exception e) {
                    return createErrorResponse("Formato de fecha inválido. Use YYYY-MM-DD");
                }
            }

            // Guardar usuario
            Usuario usuarioGuardado = usuarioService.save(nuevoUsuario);
            usuarioGuardado.setClaveHash(null); // No devolver el hash de la contraseña
            usuarioGuardado.setSalt(null);     // No devolver la sal

            // Construir respuesta
            Map<String, Object> response = new HashMap<>();
            response.put("ok", true);
            response.put("mensaje", "Usuario registrado exitosamente");
            response.put("data", usuarioGuardado);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            return createErrorResponse("Error al procesar el registro: " + e.getMessage(), 
                                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/usuario-actual")
    public ResponseEntity<Map<String, Object>> getCurrentUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            // Obtener datos actualizados del usuario
            Usuario usuario = usuarioService.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            Map<String, Object> response = new HashMap<>();
            response.put("ok", true);
            response.put("data", Map.of(
                "id", usuario.getId(),
                "correo", usuario.getCorreo(),
                "nombres", usuario.getNombres(),
                "apellidos", usuario.getApellidos(),
                "rol", usuario.getRol().name()
            ));

            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return createErrorResponse("Error al obtener la información del usuario: " + e.getMessage(), 
                                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
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

