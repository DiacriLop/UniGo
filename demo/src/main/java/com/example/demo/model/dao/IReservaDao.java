package com.example.demo.model.dao;

import com.example.demo.model.entities.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IReservaDao extends JpaRepository<Reserva, Long> {
}
