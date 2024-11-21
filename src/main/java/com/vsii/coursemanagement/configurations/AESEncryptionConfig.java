package com.vsii.coursemanagement.configurations;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Convert;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.util.Base64;

@Configuration
@Convert
@Slf4j
public class AESEncryptionConfig implements AttributeConverter<String, String> {

    @Value("${aes.encryption.key}")
    private String encryptionKey;

    private static final String ENCRYPTION_CIPHER = "AES";
    private Key key;
    private Cipher cipher;


    public Key getKey() {
        if (key == null) {
            key = new SecretKeySpec(encryptionKey.getBytes(), ENCRYPTION_CIPHER);
        }
        return key;
    }

    public Cipher getCipher() throws Exception {
        if (cipher == null) {
            cipher = Cipher.getInstance(ENCRYPTION_CIPHER);
        }
        return cipher;
    }

    @SneakyThrows
    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null) {
            return null;
        }
        try {
            Cipher cipher = getCipher();
            cipher.init(Cipher.ENCRYPT_MODE, getKey());
            String encrypted = Base64.getEncoder().encodeToString(cipher.doFinal(attribute.getBytes()));
            log.info("Encryption successful for attribute: {}", attribute);
            return encrypted;
        } catch (IllegalBlockSizeException e) {
            log.error("Encryption failed due to illegal block size: {}", e.getMessage(), e);
            throw new IllegalBlockSizeException("Encryption failed due to illegal block size: " + e.getMessage());
        } catch (InvalidKeyException e) {
            log.error("Encryption failed due to invalid key: {}", e.getMessage(), e);
            throw new InvalidKeyException("Encryption failed due to invalid key: " + e.getMessage(), e);
        } catch (GeneralSecurityException e) {
            log.error("General security exception occurred during encryption: {}", e.getMessage(), e);
            throw new GeneralSecurityException("General security exception occurred during encryption: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error during encryption: {}", e.getMessage(), e);
            throw new RuntimeException("Unexpected error during encryption: " + e.getMessage(), e);
        }
    }

    @SneakyThrows
    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        try {
            Cipher cipher = getCipher();
            cipher.init(Cipher.DECRYPT_MODE, getKey());
            String decrypted = new String(cipher.doFinal(Base64.getDecoder().decode(dbData)));
            log.info("Decryption successful for dbData.");
            return decrypted;
        } catch (IllegalBlockSizeException e) {
            log.error("Decryption failed due to illegal block size: {}", e.getMessage(), e);
            throw new IllegalBlockSizeException("Decryption failed due to illegal block size: " + e.getMessage());
        } catch (BadPaddingException e) {
            log.error("Decryption failed due to bad padding: {}", e.getMessage(), e);
            throw new BadPaddingException("Decryption failed due to bad padding: " + e.getMessage());
        } catch (InvalidKeyException e) {
            log.error("Decryption failed due to invalid key: {}", e.getMessage(), e);
            throw new InvalidKeyException("Decryption failed due to invalid key: " + e.getMessage(), e);
        } catch (GeneralSecurityException e) {
            log.error("General security exception occurred during decryption: {}", e.getMessage(), e);
            throw new GeneralSecurityException("General security exception occurred during decryption: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error during decryption: {}", e.getMessage(), e);
            throw new RuntimeException("Unexpected error during decryption: " + e.getMessage(), e);
        }
    }

}
