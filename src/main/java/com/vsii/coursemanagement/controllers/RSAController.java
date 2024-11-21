package com.vsii.coursemanagement.controllers;

import com.vsii.coursemanagement.services.implement.RSAService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.GeneralSecurityException;

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
    public String encryptMessage(@RequestBody String plainString) throws GeneralSecurityException {
        return rsaService.encryptMessage(plainString);
    }


    @PostMapping("/decrypt")
    public String decryptMessage(@RequestBody String encryptString) throws GeneralSecurityException {
        return rsaService.decryptMessage(encryptString);
    }
}