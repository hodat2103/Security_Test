package com.vsii.coursemanagement.dtos.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RSARequestDTO {
    @NotBlank(message = "Plain text must not be blank")
    @JsonProperty("plain_text")
    private String plainText;

    @NotBlank(message = "Cipher text must not be blank")
    @JsonProperty("cipher_text")
    private String cipherText;

}
