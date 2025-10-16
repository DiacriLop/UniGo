package com.example.demo.model.dao;

import com.example.demo.model.entities.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IComentarioDao extends JpaRepository<Comentario, Long> {
}
