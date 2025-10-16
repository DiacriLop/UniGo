package com.example.demo.model.service;

import com.example.demo.model.dao.IViajeDao;
import com.example.demo.model.entities.Viaje;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ViajeService {

    private final IViajeDao viajeDao;

    public ViajeService(IViajeDao viajeDao) {
        this.viajeDao = viajeDao;
    }

    @Transactional(readOnly = true)
    public List<Viaje> findAll() {
        return viajeDao.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Viaje> findById(Long id) {
        return viajeDao.findById(id);
    }

    @Transactional
    public Viaje save(Viaje viaje) {
        return viajeDao.save(viaje);
    }

    @Transactional
    public void deleteById(Long id) {
        viajeDao.deleteById(id);
    }
}
