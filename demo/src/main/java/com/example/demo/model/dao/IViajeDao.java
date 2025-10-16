package com.example.demo.model.dao;

import com.example.demo.model.entities.Viaje;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IViajeDao extends JpaRepository<Viaje, Long> {
}
