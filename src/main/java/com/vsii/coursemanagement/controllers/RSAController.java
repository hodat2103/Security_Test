package com.vsii.coursemanagement.controllers;

import com.vsii.coursemanagement.services.implement.RSAService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}rsa")
public class RSAController {

    private final RSAService rsaService;

    @GetMapping("/createKeys")
    public void createPrivatePublicKey() {
        rsaService.createKeys();
    }

    @PostMapping("/encrypt")
    public String encryptMessage(@RequestBody String plainString) throws Exception {
        return rsaService.encryptRSA(plainString);
    }


    @PostMapping("/decrypt")
    public String decryptMessage(@RequestBody String cipherString) throws Exception {
        return rsaService.decryptRSA(cipherString);
    }
}