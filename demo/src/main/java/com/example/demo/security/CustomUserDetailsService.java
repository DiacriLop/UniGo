package com.example.demo.security;

import com.example.demo.model.entities.Usuario;
import com.example.demo.model.service.UsuarioService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioService usuarioService;

    public CustomUserDetailsService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Usuario usuario = usuarioService.findByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con el correo: " + correo));
        
        return new CustomUserDetails(usuario);
    }
}
