package com.vsii.coursemanagement.services;

import com.vsii.coursemanagement.dtos.request.CourseRequestDTO;
import com.vsii.coursemanagement.dtos.response.ResponseSuccess;
import com.vsii.coursemanagement.dtos.response.data.CourseResponse;
import com.vsii.coursemanagement.exceptions.DataNotFoundException;
import com.vsii.coursemanagement.exceptions.InvalidParamException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * IService nay chua cac method public voi muc dich chua cac method de phan chia cac cong viec lien quan den khoa hoc
 */
@Service
public interface ICourseService {
    /**
     * Tao moi khoa hoc voi bo du lieu {@Link CourseRequestDTO }
     *
     * @param courseDTO dto chi tiet khoa hoc
     * @return kieu tra ve ResponseSuccess khi tao khoa hoc thanh cong
     * @throws DataNotFoundException nem ex khi loi 404
     * @throws IOException           nem ex khi loi lien quan den file
     * @throws InvalidParamException nem ex khi loi ve cac parameter khong hop le
     */
    public CourseResponse create(CourseRequestDTO courseDTO) throws DataNotFoundException, InvalidParamException;

    /**
     * method nay chuc nang lay ra tat ca va loc theo yeu cau cac khoa hoc
     *
     * @param keyword     key word tim kiem theo tieu de
     * @param categoryId  id danh muc cua khoa hoc
     * @param languageId  id ngon ngu duoc su dung trong khoa hoc
     * @param languageId  id cua nguoi huong dan khoa hoc
     * @param pageRequest dung de phan trang
     * @return ResponseSuccess  tra ve du lieu thanh cong
     */
    public ResponseSuccess getAllCourses(String keyword, Integer categoryId, Integer languageId, Integer instructorId, PageRequest pageRequest);

    /**
     * method de upload file len cloudinary
     *
     * @param courseId      course id de upload video cho khoa hoc theo id nay
     * @param multipartFile file video de upload
     * @return tra re map result chua cac thong tin ve 1 file khi duoc upload tren cloudinary
     * @throws IOException nem ex khi loi lien quan den file
     */
    public Map<String, Object> uploadFileToCloudinary(Integer courseId, MultipartFile multipartFile) throws InvalidParamException, DataNotFoundException, IOException;
}
