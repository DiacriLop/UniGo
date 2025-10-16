package com.example.demo.model.dao;

import com.example.demo.model.entities.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface INotificacionDao extends JpaRepository<Notificacion, Long> {
}
