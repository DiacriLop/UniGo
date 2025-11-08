package com.example.demo.model.dao;

import com.example.demo.model.entities.PerfilConductor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPerfilConductorDao extends JpaRepository<PerfilConductor, Long> {
}
