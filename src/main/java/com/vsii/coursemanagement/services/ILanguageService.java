package com.vsii.coursemanagement.services;

import com.vsii.coursemanagement.entities.Language;
import com.vsii.coursemanagement.exceptions.DataNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ILanguageService {
    /**
     * phuong thuc nay de lay ra tat ca cac du lieu cua instructor
     *
     * @return tra ve tat ca du lieu cua instructor
     * @throws DataNotFoundException nem ex ra khi loi url hay gia tri rong
     */
    List<Language> getAllLanguages() throws DataNotFoundException;
}
