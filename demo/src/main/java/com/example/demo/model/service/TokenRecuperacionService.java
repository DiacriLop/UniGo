package com.example.demo.model.service;

import com.example.demo.model.dao.ITokenRecuperacionDao;
import com.example.demo.model.entities.TokenRecuperacion;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TokenRecuperacionService {

    private final ITokenRecuperacionDao tokenRecuperacionDao;

    public TokenRecuperacionService(ITokenRecuperacionDao tokenRecuperacionDao) {
        this.tokenRecuperacionDao = tokenRecuperacionDao;
    }

    @Transactional(readOnly = true)
    public List<TokenRecuperacion> findAll() {
        return tokenRecuperacionDao.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<TokenRecuperacion> findById(Long id) {
        return tokenRecuperacionDao.findById(id);
    }

    @Transactional
    public TokenRecuperacion save(TokenRecuperacion tokenRecuperacion) {
        return tokenRecuperacionDao.save(tokenRecuperacion);
    }

    @Transactional
    public void deleteById(Long id) {
        tokenRecuperacionDao.deleteById(id);
    }
}
