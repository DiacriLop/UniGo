package com.example.demo.model.service;

import com.example.demo.model.dao.IPuntuacionDao;
import com.example.demo.model.entities.Puntuacion;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PuntuacionService {

    private final IPuntuacionDao puntuacionDao;

    public PuntuacionService(IPuntuacionDao puntuacionDao) {
        this.puntuacionDao = puntuacionDao;
    }

    @Transactional(readOnly = true)
    public List<Puntuacion> findAll() {
        return puntuacionDao.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Puntuacion> findById(Long id) {
        return puntuacionDao.findById(id);
    }

    @Transactional
    public Puntuacion save(Puntuacion puntuacion) {
        return puntuacionDao.save(puntuacion);
    }

    @Transactional
    public void deleteById(Long id) {
        puntuacionDao.deleteById(id);
    }
}
