package com.nus.team4.util;

import ch.qos.logback.classic.Logger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
@Slf4j
public class EncryptionUtil {

    private static final String ALGORITHM = "AES";
    private SecretKey secretKey;

    @Value("${encryption.secretKey}")
    private String base64EncodedKey;

    @PostConstruct
    public void init() {
        // Decode the base64 encoded key
        byte[] decodedKey = Base64.getDecoder().decode(base64EncodedKey);
        // Rebuild the key using SecretKeySpec
        secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, ALGORITHM);
    }

    /**
     * Encrypts a plaintext string using the AES algorithm and the generated secret key.
     *
     * @param data The plaintext string to encrypt.
     * @return A Base64 encoded string representing the encrypted data.
     */
    public String encrypt(String data) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(data.getBytes());
            log.info(java.util.Base64.getEncoder().encodeToString(encryptedBytes));
            return java.util.Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Decrypts a Base64 encoded encrypted string back to its original plaintext form.
     *
     * @param data The Base64 encoded encrypted string to decrypt.
     * @return The original plaintext string.
     */
    public String decrypt(String data) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedBytes = cipher.doFinal(java.util.Base64.getDecoder().decode(data)); // Decode the Base64 encoded data and decrypt
            return new String(decryptedBytes); // Convert the decrypted bytes back to a string and return
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
