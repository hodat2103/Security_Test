package com.vsii.coursemanagement.services;

import com.vsii.coursemanagement.entities.Instructor;
import com.vsii.coursemanagement.exceptions.DataNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IInstructorService {
    /**
     * phuong thuc nay de lay ra tat ca cac du lieu cua instructor
     *
     * @return tra ve tat ca du lieu cua instructor
     * @throws DataNotFoundException nem ex ra khi loi url hay gia tri rong
     */
    List<Instructor> getAllInstructors() throws DataNotFoundException;
}
