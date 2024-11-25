package com.vsii.coursemanagement.dtos.response.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vsii.coursemanagement.entities.Account;
import com.vsii.coursemanagement.entities.Course;
import com.vsii.coursemanagement.services.IRSAService;
import com.vsii.coursemanagement.services.implement.RSAService;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.GeneralSecurityException;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountResponse extends BaseResponse{


    private Long id;

    @JsonProperty("phone_number")
    private String phoneNumber;

    private String email;

    private String password;

    @JsonProperty("role_name")
    private String roleName;


    public static AccountResponse fromAccount(Account account) {
        AccountResponse accountResponse = AccountResponse.builder()
                .id(account.getId())
                .phoneNumber(account.getPhoneNumber())
                .email(account.getEmail())
                .password(account.getPassword())
                .build();

        accountResponse.setCreatedAt(account.getCreatedAt());
        accountResponse.setUpdatedAt(account.getUpdatedAt());
        return accountResponse;
    }
}
