package com.vsii.coursemanagement.services;

import com.vsii.coursemanagement.entities.Category;
import com.vsii.coursemanagement.exceptions.DataNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ICategoryService {
    /**
     * phuong thuc nay de lay ra tat ca cac du lieu cua category
     *
     * @return tra ve tat ca du lieu cua category
     * @throws DataNotFoundException nem ex ra khi loi url hay gia tri rong
     */
    List<Category> getAllCategories() throws DataNotFoundException;
}
