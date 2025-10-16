package com.example.demo.model.dao;

import com.example.demo.model.entities.TokenRecuperacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITokenRecuperacionDao extends JpaRepository<TokenRecuperacion, Long> {
}
