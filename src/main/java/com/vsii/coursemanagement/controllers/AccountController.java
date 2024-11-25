package com.vsii.coursemanagement.controllers;

import com.vsii.coursemanagement.dtos.request.LoginRequestDTO;
import com.vsii.coursemanagement.dtos.request.RegisterRequestDTO;
import com.vsii.coursemanagement.dtos.response.ResponseSuccess;
import com.vsii.coursemanagement.dtos.response.data.AccountResponse;
import com.vsii.coursemanagement.entities.Account;
import com.vsii.coursemanagement.exceptions.DataNotFoundException;
import com.vsii.coursemanagement.exceptions.InvalidParamException;
import com.vsii.coursemanagement.exceptions.PermissionDenyException;
import com.vsii.coursemanagement.services.IAccountService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.security.GeneralSecurityException;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}accounts")
//@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class AccountController {

    private final IAccountService accountService;

    @PostMapping("/register")
    public ResponseEntity<?> create(
            @Valid @RequestBody RegisterRequestDTO registerRequestDTO,
            BindingResult result
    ) throws DataNotFoundException, PermissionDenyException, GeneralSecurityException,Exception {

        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body("Error: " + result.getFieldErrors());

        }
        Account account = accountService.register(registerRequestDTO);
        ResponseSuccess responseSuccess = new ResponseSuccess(HttpStatus.ACCEPTED, "Register account successfully", account);
        return ResponseEntity.ok(responseSuccess);

    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginRequestDTO loginRequestDTO
    ) throws Exception {

        String token = accountService.login(loginRequestDTO.getPhoneNumber(), loginRequestDTO.getPassword());
        ResponseSuccess responseSuccess = new ResponseSuccess(HttpStatus.OK,"Login successfully: " + token );
        return ResponseEntity.ok(responseSuccess);

    }

    @GetMapping("")
    public ResponseEntity<?> getAccount(@Pattern(regexp = "0\\d{9}") @RequestParam("phone_number") String phoneNumber) throws DataNotFoundException, GeneralSecurityException,Exception {
        AccountResponse accountResponse = accountService.getAccountById(phoneNumber);
        ResponseSuccess responseSuccess = new ResponseSuccess(HttpStatus.OK,"Retrieve account successfully", accountResponse );
        return ResponseEntity.ok(responseSuccess);
    }

}
