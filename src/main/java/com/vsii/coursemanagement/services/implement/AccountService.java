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

    /**
     * Method de dang kt tai khoan cho nguoi dung
     *
     * @Param {@link RegisterRequestDTO} da duoc validate
     * @Return tra ve object Account tai khoan moi duoc tao
     * @throws PermissionDenyException khi khong phu hop voi quyen admin trong role database
     * @throws DataNotFoundException khi khong tim thay role_name theo role_id
     * @throws DataIntegrityViolationException khi khong ton tai sdt trong database
     */
    @Override
    public Account register(RegisterRequestDTO registerRequestDTO) throws PermissionDenyException,DataNotFoundException,DataIntegrityViolationException{
        String phoneNumber = registerRequestDTO.getPhoneNumber();
        if(accountRepository.existsByPhoneNumber(phoneNumber)){
            throw new DataIntegrityViolationException("Phone number already exists");
        }
        Role role = roleRepository.findById(registerRequestDTO.getRoleId())
                .orElseThrow(() -> new DataNotFoundException(""));
//        if (role.getName() != null && role.getName().toLowerCase().equals("ADMIN")) {
//            throw new PermissionDenyException("Cannot register an admin account");
//        }

        Account newAccount  = Account.builder()
                .phoneNumber(registerRequestDTO.getPhoneNumber())
                .email(registerRequestDTO.getEmail())
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

    /**
     * Method dang nhap tai khoan nguoi dung
     * @param phoneNumber sdt tai khoan nguoi dung
     * @param password mat khau tai khoab
     * @return token moi tao duoc
     * @throws DataNotFoundException khi khong tim thay tai khoan trong DB hoac tai khoan nay khong hoat dong nua
     */
    @Override
    public String login(String phoneNumber, String password) throws DataNotFoundException {
        Optional<Account> optionalAccount = accountRepository.findByPhoneNumber(phoneNumber);
        if (optionalAccount.isEmpty()) {
            throw new DataNotFoundException("Not found account");
        }
        Account existingAccount = optionalAccount.get();

        if (!existingAccount.isActive()) {
            throw new DataNotFoundException("Not found account");
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                phoneNumber, password, existingAccount.getAuthorities()
        );

        // Authenticate user with Spring Security
        authenticationManager.authenticate(authenticationToken);

        return jwtTokenUtils.generateToken(existingAccount);
    }


//    @Override
//    public UserDetails loadUserByUsername(String phoneNumber) {
//        return accountRepository.findByPhoneNumber(phoneNumber)
//                .orElseThrow(() -> new UsernameNotFoundException("Account not found"));
//    }

    /**
     * Method tim account theo id
     * @param id cua account
     * @return tra ve 1 object account theo id
     * @throws DataNotFoundException khi khong tim thay account theo id yeu cau
     */
    public Account getAccountById(Long id) throws DataNotFoundException {
        return accountRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Account not found"));
    }

}
