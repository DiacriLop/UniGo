package com.example.demo.model.dao;

import com.example.demo.model.entities.Puntuacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPuntuacionDao extends JpaRepository<Puntuacion, Long> {
}
