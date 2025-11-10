package com.example.demo.model.service;

import com.example.demo.model.dao.IUsuarioDao;
import com.example.demo.model.entities.Usuario;
import com.example.demo.security.AESPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final IUsuarioDao usuarioDao;
    private final AESPasswordEncoder passwordEncoder;

    public UsuarioService(IUsuarioDao usuarioDao, AESPasswordEncoder passwordEncoder) {
        this.usuarioDao = usuarioDao;
        this.passwordEncoder = passwordEncoder;
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

        // 🔹 Generar salt y encriptar la clave antes de guardar
        String salt = passwordEncoder.generateSalt();
        String encryptedPassword = passwordEncoder.encryptPassword(usuario.getClaveHash(), salt);
        
        usuario.setClaveHash(encryptedPassword);
        usuario.setSalt(salt);
        return usuarioDao.save(usuario);
    }

    public boolean verificarClave(Usuario usuario, String clave) {
        if (usuario.getSalt() == null || usuario.getClaveHash() == null) {
            return false;
        }
        return passwordEncoder.verifyPassword(clave, usuario.getClaveHash(), usuario.getSalt());
    }

    @Transactional
    public void actualizarClave(Usuario usuario, String nuevaClave) {
        String salt = passwordEncoder.generateSalt();
        String encryptedPassword = passwordEncoder.encryptPassword(nuevaClave, salt);
        
        usuario.setClaveHash(encryptedPassword);
        usuario.setSalt(salt);
        usuarioDao.save(usuario);
    }

    @Transactional
    public void deleteById(Long id) {
        usuarioDao.deleteById(id);
    }
}

