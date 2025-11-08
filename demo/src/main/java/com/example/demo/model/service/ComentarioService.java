package com.example.demo.model.service;

import com.example.demo.model.dao.IComentarioDao;
import com.example.demo.model.entities.Comentario;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ComentarioService {

    private final IComentarioDao comentarioDao;

    public ComentarioService(IComentarioDao comentarioDao) {
        this.comentarioDao = comentarioDao;
    }

    @Transactional(readOnly = true)
    public List<Comentario> findAll() {
        return comentarioDao.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Comentario> findById(Long id) {
        return comentarioDao.findById(id);
    }

    @Transactional
    public Comentario save(Comentario comentario) {
        return comentarioDao.save(comentario);
    }

    @Transactional
    public void deleteById(Long id) {
        comentarioDao.deleteById(id);
    }
}
