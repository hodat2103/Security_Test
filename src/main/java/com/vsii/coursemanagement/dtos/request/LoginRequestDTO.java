package com.vsii.coursemanagement.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoginRequestDTO {

    @NotBlank(message = "Phone number is required")
    @Size(min = 10, max = 10, message = "Phone number must be exactly 10 digits")
    @Pattern(regexp = "0\\d{9}", message = "Phone number must start with 0 and contain 10 digits")
    private String phoneNumber;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 6, max = 255, message = "Password must be between 6 and 255 characters")
    private String password;
}