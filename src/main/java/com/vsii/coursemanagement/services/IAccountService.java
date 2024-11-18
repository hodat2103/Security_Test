package com.vsii.coursemanagement.services;

import com.vsii.coursemanagement.dtos.request.RegisterRequestDTO;
import com.vsii.coursemanagement.entities.Account;
import com.vsii.coursemanagement.exceptions.DataNotFoundException;
import com.vsii.coursemanagement.exceptions.InvalidParamException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


@Service
public interface IAccountService {

    Account register(RegisterRequestDTO registerRequestDTO) throws Exception;

    String login(String phoneNumber, String password) throws DataNotFoundException, InvalidParamException;

    public UserDetails loadUserByUsername(String username);
}
