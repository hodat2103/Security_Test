package com.vsii.coursemanagement.services;

import com.vsii.coursemanagement.dtos.request.RegisterRequestDTO;
import com.vsii.coursemanagement.entities.Account;
import com.vsii.coursemanagement.exceptions.DataNotFoundException;
import com.vsii.coursemanagement.exceptions.InvalidParamException;
import com.vsii.coursemanagement.exceptions.PermissionDenyException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * IService nay chua cac method public voi muc dich chua cac method de phan chia cac cong viec lien quan den tai khoan nguoi dung
 */

@Service
public interface IAccountService {
    /**
     * Method de dang kt tai khoan cho nguoi dung
     *
     * @Param {@link RegisterRequestDTO} da duoc validate
     * @Return tra ve object Account tai khoan moi duoc tao
     * @throws PermissionDenyException khi khong phu hop voi quyen admin trong role database
     * @throws DataNotFoundException khi khong tim thay role_name theo role_id
     * @throws DataIntegrityViolationException khi khong ton tai sdt trong database
     */
    public Account register(RegisterRequestDTO registerRequestDTO) throws PermissionDenyException,DataNotFoundException,DataIntegrityViolationException;

    /**
     * Method dang nhap tai khoan nguoi dung
     * @param phoneNumber sdt tai khoan nguoi dung
     * @param password mat khau tai khoab
     * @return token moi tao duoc
     * @throws DataNotFoundException khi khong tim thay tai khoan trong DB hoac tai khoan nay khong hoat dong nua
     */
    public String login(String phoneNumber, String password) throws DataNotFoundException;

//    public UserDetails loadUserByPhoneNumber(String username);
    /**
     * Method tim account theo id
     * @param id cua account
     * @return tra ve 1 object account theo id
     * @throws DataNotFoundException khi khong tim thay account theo id yeu cau
     */
    public Account getAccountById(Long id) throws DataNotFoundException;
}
