package com.vsii.coursemanagement.controllers;

import com.vsii.coursemanagement.dtos.request.LoginRequestDTO;
import com.vsii.coursemanagement.dtos.request.RegisterRequestDTO;
import com.vsii.coursemanagement.entities.Account;
import com.vsii.coursemanagement.exceptions.InvalidParamException;
import com.vsii.coursemanagement.services.IAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/accounts")
//@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class AccountController {
    private final IAccountService accountService;
//    @CrossOrigin(origins = "http://localhost:**")
    @PostMapping("/register")
    public ResponseEntity<?> create (
            @Valid @RequestBody RegisterRequestDTO registerRequestDTO,
            BindingResult result
    ) throws Exception {

            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body("Loi abc: " + result.getFieldErrors());

            }
            Account account =  accountService.register(registerRequestDTO);
            return  ResponseEntity.ok(account);

    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginRequestDTO loginRequestDTO
    ) throws InvalidParamException {
        try {
            String token = accountService.login(loginRequestDTO.getUsername(), loginRequestDTO.getPassword());
            return ResponseEntity.ok("Login ok: " + token);
        } catch(Exception e) {
            return ResponseEntity.ok("");
        }
    }


}
