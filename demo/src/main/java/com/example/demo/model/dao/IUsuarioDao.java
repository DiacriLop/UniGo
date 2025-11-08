package com.example.demo.model.dao;

import com.example.demo.model.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface IUsuarioDao extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByCorreo(String correo);
}

