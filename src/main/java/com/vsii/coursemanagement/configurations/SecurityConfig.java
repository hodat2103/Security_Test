package com.vsii.coursemanagement.configurations;

import com.nimbusds.jose.Algorithm;
import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import com.vsii.coursemanagement.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.KeyStore;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final AccountRepository accountRepository;
    @Value("${jwt.encryption.keystore.type:JKS}")
    String keystoreType;

    @Value("${jwt.encryption.keystore.path:keystore.jks}")
    String keystorePath;

    @Value("${jwt.encryption.keystore.password:geheim}")
    String keystorePassword;

    @Bean
    public UserDetailsService userDetailsService() {
        return phoneNumber -> accountRepository
                .findByPhoneNumber(phoneNumber)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "Cannot find account with phone number = " + phoneNumber));

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    KeyStore signingKeystore() throws Exception {

        KeyStore keyStore = KeyStore.getInstance(keystoreType);
        char[] password = keystorePassword.toCharArray();
        keyStore.load(getClass().getClassLoader().getResourceAsStream(keystorePath), password);

        return keyStore;
    }

    @Bean
    RSAKey jwsSigningKey(KeyStore signingKeystore) throws Exception {

        char[] password = keystorePassword.toCharArray();
        RSAKey key = RSAKey.load(signingKeystore, "jwt-signing", password);

        RSAKey signingKey = new RSAKey.Builder(key)
                .keyID("jwt-signing")
                .keyUse(KeyUse.SIGNATURE)
                .algorithm(new Algorithm("RS256"))
                .build();

        return signingKey;
    }

    @Bean
    SecretKey encryptionKey() throws Exception {

        // Get the expected key length for JWE enc "A128CBC-HS256"
        int keyBitLength = EncryptionMethod.A256GCM.cekBitLength();

        // Generate key
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(keyBitLength);
        SecretKey key = keyGen.generateKey();

        return key;
    }

    @Bean
    TokenManager tokenManager(RSAKey jwsSigningKey, SecretKey jweEncryptionKey) {
        return new TokenManager(jwsSigningKey, jweEncryptionKey);
    }
}
