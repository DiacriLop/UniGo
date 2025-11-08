package com.example.demo.model.service;

import com.example.demo.model.dao.IReservaDao;
import com.example.demo.model.entities.Reserva;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ReservaService {

    private final IReservaDao reservaDao;

    public ReservaService(IReservaDao reservaDao) {
        this.reservaDao = reservaDao;
    }

    @Transactional(readOnly = true)
    public List<Reserva> findAll() {
        return reservaDao.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Reserva> findById(Long id) {
        return reservaDao.findById(id);
    }

    @Transactional
    public Reserva save(Reserva reserva) {
        return reservaDao.save(reserva);
    }

    @Transactional
    public void deleteById(Long id) {
        reservaDao.deleteById(id);
    }
}
