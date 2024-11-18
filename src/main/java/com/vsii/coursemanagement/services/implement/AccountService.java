package com.vsii.coursemanagement.services.implement;

import com.vsii.coursemanagement.components.JwtTokenUtils;
import com.vsii.coursemanagement.dtos.request.RegisterRequestDTO;
import com.vsii.coursemanagement.entities.Account;
import com.vsii.coursemanagement.entities.Role;
import com.vsii.coursemanagement.exceptions.DataNotFoundException;
import com.vsii.coursemanagement.exceptions.InvalidParamException;
import com.vsii.coursemanagement.exceptions.PermissionDenyException;
import com.vsii.coursemanagement.repositories.AccountRepository;
import com.vsii.coursemanagement.repositories.RoleRepository;
import com.vsii.coursemanagement.services.IAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService implements IAccountService {
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final JwtTokenUtils jwtTokenUtils;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;


    @Override
    public Account register(RegisterRequestDTO registerRequestDTO) throws Exception{
        String phoneNumber = registerRequestDTO.getUsername();
        if(accountRepository.existsByUsername(phoneNumber)){
            throw new DataIntegrityViolationException("Phone number already exists");
        }
        Role role = roleRepository.findById(registerRequestDTO.getRoleId())
                .orElseThrow(() -> new DataNotFoundException(""));
        if (role.getName() != null && role.getName().equalsIgnoreCase("ADMIN")) {
            throw new PermissionDenyException("Cannot register an admin account");
        }
        Account newAccount  = Account.builder()
                .username(registerRequestDTO.getUsername())
                .password(registerRequestDTO.getPassword())
                .role(role)
                .active(true)
                .build();
        String password = registerRequestDTO.getPassword();
        String encodePassword = passwordEncoder.encode(password);
        newAccount.setPassword(encodePassword);
        accountRepository.save(newAccount);


        return newAccount;
    }

    @Override
    public String login(String username, String password) throws DataNotFoundException, InvalidParamException {
        Optional<Account> optionalAccount = accountRepository.findByUsername(username);
        if (optionalAccount.isEmpty()) {
            throw new DataNotFoundException("");
        }
        Account existingAccount = optionalAccount.get();

        if (!existingAccount.isActive()) {
            throw new DataNotFoundException("");
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                username, password, existingAccount.getAuthorities()
        );

        // Authenticate user with Spring Security
        authenticationManager.authenticate(authenticationToken);

        return jwtTokenUtils.generateToken(existingAccount);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return accountRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Account not found"));
    }

}
