package com.example.demo.model.dao;

import com.example.demo.model.entities.Pago;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPagoDao extends JpaRepository<Pago, Long> {
}
