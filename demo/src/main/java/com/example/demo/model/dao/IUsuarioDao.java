package com.example.demo.model.dao;

import com.example.demo.model.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUsuarioDao extends JpaRepository<Usuario, Long> {
}
