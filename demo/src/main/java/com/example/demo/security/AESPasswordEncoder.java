package com.example.demo.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AESPasswordEncoder implements PasswordEncoder {

    private final AESEncryptionService encryptionService;

    public AESPasswordEncoder(AESEncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }

    @Override
    public String encode(CharSequence rawPassword) {
        // This method won't be used directly as we'll handle encryption in the UserService
        throw new UnsupportedOperationException("Use UserService.encryptPassword instead");
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        // This method won't be used directly as we'll handle decryption in the UserService
        throw new UnsupportedOperationException("Use UserService.verifyPassword instead");
    }

    public String encryptPassword(String password, String salt) {
        try {
            return encryptionService.encrypt(password, salt);
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting password", e);
        }
    }

    public boolean verifyPassword(String rawPassword, String encryptedPassword, String salt) {
        return encryptionService.matches(rawPassword, encryptedPassword, salt);
    }

    public String generateSalt() {
        return encryptionService.generateSalt();
    }
}
