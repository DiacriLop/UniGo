package com.example.demo.model.service;

import com.example.demo.model.dao.IPerfilConductorDao;
import com.example.demo.model.entities.PerfilConductor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PerfilConductorService {

    private final IPerfilConductorDao perfilConductorDao;

    public PerfilConductorService(IPerfilConductorDao perfilConductorDao) {
        this.perfilConductorDao = perfilConductorDao;
    }

    @Transactional(readOnly = true)
    public List<PerfilConductor> findAll() {
        return perfilConductorDao.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<PerfilConductor> findById(Long id) {
        return perfilConductorDao.findById(id);
    }

    @Transactional
    public PerfilConductor save(PerfilConductor perfilConductor) {
        return perfilConductorDao.save(perfilConductor);
    }

    @Transactional
    public void deleteById(Long id) {
        perfilConductorDao.deleteById(id);
    }
}
