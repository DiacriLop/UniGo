package com.example.demo.model.service;

import com.example.demo.model.dao.IPagoDao;
import com.example.demo.model.entities.Pago;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PagoService {

    private final IPagoDao pagoDao;

    public PagoService(IPagoDao pagoDao) {
        this.pagoDao = pagoDao;
    }

    @Transactional(readOnly = true)
    public List<Pago> findAll() {
        return pagoDao.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Pago> findById(Long id) {
        return pagoDao.findById(id);
    }

    @Transactional
    public Pago save(Pago pago) {
        return pagoDao.save(pago);
    }

    @Transactional
    public void deleteById(Long id) {
        pagoDao.deleteById(id);
    }
}
