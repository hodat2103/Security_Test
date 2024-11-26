package com.vsii.coursemanagement.utils;

import com.vsii.coursemanagement.services.IRSAService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class RSAUtil {
    private static final String PUBLIC_KEY_FILE = "publicKey.pem";
    private static final String PRIVATE_KEY_FILE = "privateKey.pem";
    private static final int KEY_SIZE = 4096;
    private static final String ALGORITHM = "RSA";
    private static final String TRANSFORMATION_CIPHER = "RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING";
    private static final Map<String, Object> map = new HashMap<>();

    @PostConstruct
    public void initializeKeys() {
        try {
            if (Files.exists(Paths.get(PUBLIC_KEY_FILE)) && Files.exists(Paths.get(PRIVATE_KEY_FILE))) {
                log.info("Key files found. Loading keys from files.");
                PublicKey publicKey = loadPublicKey(PUBLIC_KEY_FILE);
                PrivateKey privateKey = loadPrivateKey(PRIVATE_KEY_FILE);

                map.put("publicKey", publicKey);
                map.put("privateKey", privateKey);
            } else {
                log.info("Key files not found. Generating new key pair.");
                createKeys();
            }
        } catch (Exception e) {
            log.error("Error initializing keys: ", e.getMessage());
        }
    }

    public void createKeys() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
            keyPairGenerator.initialize(KEY_SIZE);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            // save key pair to memory
            map.put("publicKey", publicKey);
            map.put("privateKey", privateKey);

            // save key pair to file
            saveKeyToFile(PUBLIC_KEY_FILE, publicKey.getEncoded());
            saveKeyToFile(PRIVATE_KEY_FILE, privateKey.getEncoded());

            log.info("Key pair generated and saved to files.");
        } catch (Exception e) {
            log.error("Error generating key pair: ", e);
        }
    }

    private void saveKeyToFile(String fileName, byte[] key) throws Exception {
        // Encode key to Base64 and write to file
        String encodedKey = Base64.getEncoder().encodeToString(key);
        Files.write(Paths.get(fileName), encodedKey.getBytes());
        log.info("Key saved to file: {}", fileName);
    }

    private PublicKey loadPublicKey(String fileName) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(Files.readAllBytes(Paths.get(fileName)));
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        return keyFactory.generatePublic(spec);
    }

    private PrivateKey loadPrivateKey(String fileName) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(Files.readAllBytes(Paths.get(fileName)));
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        return keyFactory.generatePrivate(spec);
    }

    public String encryptRSA(String plainText) throws InvalidKeyException,GeneralSecurityException,Exception {
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION_CIPHER);
            PublicKey publicKey = (PublicKey) map.get("publicKey");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            byte[] encrypt = cipher.doFinal(plainText.getBytes());
            return Base64.getEncoder().encodeToString(encrypt);
        } catch (InvalidKeyException e) {
            log.error("Encryption failed: Invalid public key", e);
            throw new InvalidKeyException("Encryption failed: Invalid public key", e);
        } catch (GeneralSecurityException e) {
            log.error("Encryption failed: Security issue", e);
            throw new GeneralSecurityException("Encryption failed: Security issue", e);
        } catch (Exception e) {
            log.error("Encryption failed: Unexpected error", e);
            throw new RuntimeException("Encryption failed: Unexpected error", e);
        }
    }

    public String decryptRSA(String cipherText) throws IllegalArgumentException,InvalidKeyException,GeneralSecurityException,Exception {
        try {
            if(cipherText == null){
                throw new NullPointerException("Cipher must not be null");
            }
            // guaranteeing string valid in the code table Base 64
            cipherText = cipherText.replaceAll("[^A-Za-z0-9+/=]", "");
            byte[] decodedBytes = Base64.getDecoder().decode(cipherText);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION_CIPHER);
            PrivateKey privateKey = (PrivateKey) map.get("privateKey");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            byte[] decrypt = cipher.doFinal(decodedBytes);
            return new String(decrypt);
        } catch (IllegalArgumentException e) {
            log.warn("Decryption failed: Invalid Base64 input "+ e.getMessage());
            throw new IllegalArgumentException("Decryption failed: Invalid Base64 input "+ e.getMessage());
        } catch (InvalidKeyException e) {
            log.error("Decryption failed: Invalid private key " + e.getMessage());
            throw new InvalidKeyException("Decryption failed: Invalid private key "+ e.getMessage());
        } catch (GeneralSecurityException e) {
            log.error("Decryption failed: Security issue "+ e.getMessage());
            throw new GeneralSecurityException("Decryption failed: Security issue "+ e.getMessage());
        } catch (Exception e) {
            log.error("Decryption failed: Unexpected error "+ e.getMessage());
            throw new RuntimeException("Decryption failed: Unexpected error "+ e.getMessage());
        }
    }
}