package com.vsii.coursemanagement.dtos.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RegisterRequestDTO {
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @NotBlank(message = "Password can't blank")
    @Size(min = 6, max = 255, message = "Password must be between 6 and 255 characters")
    private String password;

    @JsonProperty("retype_password")
    @Size(min = 6, max = 255, message = "Password must be between 6 and 255 characters")
    private String retypePassword;

    @JsonProperty("role_id")
    @Min(value = 1, message = "Role Id must be positive integer")
    private Long roleId;

}
