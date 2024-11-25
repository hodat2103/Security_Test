package com.vsii.coursemanagement.entities;

import com.vsii.coursemanagement.configurations.AESEncryptionConfig;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "account")
@Builder
@Data
public class Account extends BaseEntity implements UserDetails, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "phone_number", length = 10, unique = true)
    private String phoneNumber;

//    @Email
    @Column(name = "email")
//    @Convert(converter = AESEncryptionConfig.class)
    private String email;

    @Column(name = "password", length = 255)
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(name = "active")
    private boolean active;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.getRole().getName().toUpperCase()));

        this.getRole().getPermissions().forEach(permission ->
                authorities.add(new SimpleGrantedAuthority(permission.getName()))
        );

        return authorities;
    }

    @Override
    public String getUsername() {
        return phoneNumber;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }

}
