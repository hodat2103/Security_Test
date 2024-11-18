package com.vsii.coursemanagement.services.implement;

import com.vsii.coursemanagement.components.Translator;
import com.vsii.coursemanagement.entities.Instructor;
import com.vsii.coursemanagement.exceptions.DataNotFoundException;
import com.vsii.coursemanagement.repositories.InstructorRepository;
import com.vsii.coursemanagement.services.IInstructorService;
import com.vsii.coursemanagement.utils.MessageKey;
import org.springframework.beans.factory.annotation.Autowired;
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
     *
     * @return tra ve (http code, massage or ( data)) theo ket qua tuong ung
     */
    @Override
    public List<Instructor> getAllInstructors() throws DataNotFoundException {


            List<Instructor> instructors = instructorRepository.findAll();

            if (instructors.isEmpty()) {
                // tra ve voi ma 404 khong co du lieu
                throw new DataNotFoundException(Translator.toLocale(MessageKey.NO_INSTRUCTORS_FOUND));
            }

            // tra ve danh sach co du lieu
            return instructors;

    }
}
