package com.vsii.coursemanagement.services;

import com.vsii.coursemanagement.dtos.response.ResponseSuccess;
import org.springframework.stereotype.Service;

@Service
public interface ICategoryService {
    /**
     * phuong thuc nay de lay ra tat ca cac du lieu cua category
     * @return tra ve tat ca du lieu cua category
     */
    ResponseSuccess getAllCategories();
}
