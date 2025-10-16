package com.example.demo.model.service;

import com.example.demo.model.dao.IUsuarioDao;
import com.example.demo.model.entities.Usuario;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final IUsuarioDao usuarioDao;

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

    @Transactional
    public Usuario save(Usuario usuario) {
        return usuarioDao.save(usuario);
    }

    @Transactional
    public void deleteById(Long id) {
        usuarioDao.deleteById(id);
    }
}
