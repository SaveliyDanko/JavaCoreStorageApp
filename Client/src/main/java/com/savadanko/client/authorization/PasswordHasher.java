package com.savadanko.client.authorization;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class PasswordHasher {
    private static final String PEPPER = "fixedSecretPepper"; // Фиксированный секретный перец

    // Генерация случайного соли
    public static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    // Хэширование пароля с использованием соли и перца
    public static byte[] hashPassword(String password, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        if (salt != null) {
            md.update(salt); // Добавление соли
        }
        md.update(PEPPER.getBytes()); // Добавление перца
        return md.digest(password.getBytes());
    }
}
