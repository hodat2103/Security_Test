package com.vsii.coursemanagement.services.implement;

import com.vsii.coursemanagement.configurations.Translator;
import com.vsii.coursemanagement.dtos.response.ResponseSuccess;
import com.vsii.coursemanagement.entities.Instructor;
import com.vsii.coursemanagement.repositories.InstructorRepository;
import com.vsii.coursemanagement.services.IInstructorService;
import com.vsii.coursemanagement.utils.MessageKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * Service tiep nhan cac cong viec tu IService nay xu ly cac logic lien quan den instructor
 */
@Service
public class InstructorService implements IInstructorService {

    @Autowired
    private InstructorRepository instructorRepository;

    /**
     * method de lay ra tat ca cac ngon ngu trong bang du lieu instructor
     * @return tra ve (http code, massage or ( data)) theo ket qua tuong ung
     */
    @Override
    public ResponseSuccess getAllInstructors() {

        try {
            List<Instructor> instructors = instructorRepository.findAll();

            if (instructors.isEmpty()) {
                // tra ve voi ma 204 khong co du lieu
                return new ResponseSuccess(HttpStatus.NO_CONTENT, Translator.toLocale(MessageKey.NO_INSTRUCTORS_FOUND));
            }

            // tra ve danh sach co du lieu
            return new ResponseSuccess(HttpStatus.OK, Translator.toLocale(MessageKey.INSTRUCTORS_RETRIEVE_SUCCESSFULLY), instructors);

        } catch (DataAccessException e) {
            // nem ra loi khi loi truy cap database
            throw new RuntimeException(Translator.toLocale(MessageKey.DATABASE_ERROR)+ e.getMessage(), e);
        } catch (Exception e) {
            // xu ly cac loi exception
            throw new RuntimeException(Translator.toLocale(MessageKey.EXCEPTION_COMMON) + e.getMessage(), e);
        }
    }
}
