package com.example.demo.model.service;

import com.example.demo.model.dao.IUsuarioDao;
import com.example.demo.model.entities.Usuario;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final IUsuarioDao usuarioDao;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UsuarioService(IUsuarioDao usuarioDao) {
        this.usuarioDao = usuarioDao;
    }

    @Transactional(readOnly = true)
    public List<Usuario> findAll() {
        return usuarioDao.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> findById(Long id) {
        return usuarioDao.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> findByCorreo(String correo) {
        return usuarioDao.findByCorreo(correo);
    }

    @Transactional
    public Usuario save(Usuario usuario) {
        // 🔹 Validar si el correo ya existe
        if (usuarioDao.findByCorreo(usuario.getCorreo()).isPresent()) {
            throw new RuntimeException("El correo ya está registrado");
        }

        // 🔹 Hashear la clave antes de guardar
        usuario.setClaveHash(passwordEncoder.encode(usuario.getClaveHash()));
        return usuarioDao.save(usuario);
    }

    public boolean verificarClave(Usuario usuario, String clave) {
        return passwordEncoder.matches(clave, usuario.getClaveHash());
    }

    @Transactional
    public void actualizarClave(Usuario usuario, String nuevaClave) {
        usuario.setClaveHash(passwordEncoder.encode(nuevaClave));
        usuarioDao.save(usuario);
    }

    @Transactional
    public void deleteById(Long id) {
        usuarioDao.deleteById(id);
    }
}

