package com.vsii.coursemanagement.configurations;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;

import javax.crypto.SecretKey;

@RequiredArgsConstructor
public class TokenManager {

    private final RSAKey signingKey; // RSA to sign JWT

    private final SecretKey encryptionKey; // AES used to encrypt JWT signed

    /**
     * Tạo một token JWT đã ký và mã hóa.
     *
     * @param claims Dữ liệu payload chứa các tuyên bố JWT.
     * @return Chuỗi JWT đã ký và mã hóa.
     * @throws Exception Nếu xảy ra lỗi trong quá trình ký hoặc mã hóa.
     */
    public String toEncryptedToken(JWTClaimsSet claims) throws Exception {
//        System.out.println(encryptionKey);
        // create object SignedJWT with header & claims
        SignedJWT signedJWT = new SignedJWT(
                new JWSHeader.Builder(JWSAlgorithm.RS256)
                        .keyID(signingKey.getKeyID())
                        .build(),
                claims);

        // sign token JWT by  separate RSA key
        signedJWT.sign(new RSASSASigner(signingKey));

        // create object JWEObject to encrypt JWT signed
        JWEObject jweObjectOutput = new JWEObject(
                new JWEHeader.Builder(JWEAlgorithm.DIR, EncryptionMethod.A256GCM)
                        .contentType("JWT")
                        .build(),
                new Payload(signedJWT));

        // Encrypt JWT by DirectEncrypter with AES key
        jweObjectOutput.encrypt(new DirectEncrypter(encryptionKey));

        // serialization object JWE convert into string
        String jweString = jweObjectOutput.serialize();

//        System.out.println(jweString);

        return jweString;
    }


    /**
     * Phân tích và xác thực token JWT đã ký và mã hóa.
     *
     * @param encryptedToken Chuỗi JWT đã mã hóa.
     * @return Các tuyên bố JWT (JWTClaimsSet) sau khi xác thực.
     * @throws Exception Nếu chữ ký không hợp lệ hoặc xảy ra lỗi khi giải mã.
     */
    public JWTClaimsSet parseEncryptedToken(String encryptedToken) throws Exception {
//        System.out.println(encryptionKey);
        // Get public key from RSA key  to verify signature
        RSAKey senderPublicJWK = signingKey.toPublicJWK();

        // Analyst JWT string signed convert into object JWEObject
        JWEObject jweObjectInput = JWEObject.parse(encryptedToken);

        // Decrypt JWEObject by AES key (DirectDecrypter)
        jweObjectInput.decrypt(new DirectDecrypter(encryptionKey));

        // Get payload from JWEObject (JWT signed)
        Payload payload = jweObjectInput.getPayload();

        // create JWSVerifier to verify signature of JWT
        JWSVerifier verifier = new RSASSAVerifier(senderPublicJWK);

        // Convert payload into object SignedJWT
        SignedJWT signedJWT = payload.toSignedJWT();

        // Verify signature of JWT
        if (!signedJWT.verify(verifier)) {
            throw new Exception("Bad token signature");
        }

        // Statements  (claims) from JWT verified
        return signedJWT.getJWTClaimsSet();
    }

}