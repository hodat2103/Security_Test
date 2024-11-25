package com.vsii.coursemanagement.services;

import org.springframework.stereotype.Service;

import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;

@Service
public interface IRSAService {
    /**
     * Method: muc dinh neu cap khoa da duoc tao va luu xuong file
     *           thi moi lan ma hoa chi can goi method nay ma khong can phai tao cap khoa moi
     *
     */
    public void initializeKeys();

    /**
     * Method: tao ra cap khoa pulic key va private key va cap khoa se duoc luu vao bo nho va file luu tru
     */
    public void createKeys();

    /**
     * Method: de ma hoa thong diep bang public key do recipient cung cap
     *
     * @param plainText thong diep doc chua duoc ma hoa
     * @return tra ve cipherText la thong diep da duoc ma hoa bang public key
     * @throws GeneralSecurityException neu xay ra cac loi ve bao mat van de voi ma hoa, giai ma hoac cac thao tac bao mat khac
     * @throws InvalidKeyException neu khoa khong hop le
     * @throws Exception bat loi chung cua method
     */
    public String encryptRSA(String plainText) throws InvalidKeyException,GeneralSecurityException, Exception;

    /**
     * Method: de giai ma thong diep da duoc ma hoa (public key) bang private key
     *
     * @param cipherText thong diep da ma hoa bang public key
     * @return tra ve plainText da duoc giai ma bang private key
     * @throws GeneralSecurityException neu xay ra cac loi ve bao mat van de voi ma hoa, giai ma hoac cac thao tac bao mat khac
     * @throws IllegalArgumentException neu loi va chuyen doi Base 84 khi dua thong tin da ma hoa vao khong hop le
     * @throws InvalidKeyException neu khoa khong hop le
     * @throws Exception bat loi chung cua method
     */
    public String decryptRSA(String cipherText) throws GeneralSecurityException,IllegalArgumentException,InvalidKeyException, Exception;
}
