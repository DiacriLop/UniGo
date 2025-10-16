package com.example.demo.model.service;

import com.example.demo.model.dao.INotificacionDao;
import com.example.demo.model.entities.Notificacion;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class NotificacionService {

    private final INotificacionDao notificacionDao;

    public NotificacionService(INotificacionDao notificacionDao) {
        this.notificacionDao = notificacionDao;
    }

    @Transactional(readOnly = true)
    public List<Notificacion> findAll() {
        return notificacionDao.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Notificacion> findById(Long id) {
        return notificacionDao.findById(id);
    }

    @Transactional
    public Notificacion save(Notificacion notificacion) {
        return notificacionDao.save(notificacion);
    }

    @Transactional
    public void deleteById(Long id) {
        notificacionDao.deleteById(id);
    }
}
