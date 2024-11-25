package com.vsii.coursemanagement.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "rsa_text")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RSA {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "plain_text")
    private String plainText;

    @Column(name = "cipher_text")
    private String cipherText;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

}
