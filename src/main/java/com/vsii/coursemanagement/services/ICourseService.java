package com.vsii.coursemanagement.services;

import com.vsii.coursemanagement.dtos.request.CourseRequestDTO;
import com.vsii.coursemanagement.dtos.response.data.CourseResponse;
import com.vsii.coursemanagement.exceptions.DataNotFoundException;
import com.vsii.coursemanagement.exceptions.InvalidParamException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * IService nay chua cac method public voi muc dich chua cac method de phan chia cac cong viec lien quan den khoa hoc
 */
@Service
public interface ICourseService {
    /**
     * Function: Tao moi khoa hoc voi bo du lieu {@link CourseRequestDTO }
     *
     * @param courseDTO dto chi tiet khoa hoc
     * @return kieu tra ve {@link CourseResponse} khi tao khoa hoc thanh cong
     * @throws DataNotFoundException nem ex khi loi 404 (sai duong dan hoac khong truyen vao tham so cho method)
     * @throws IOException           nem ex khi loi lien quan den file (khi file khong doc duoc hay khong tim thay file)
     */
    public CourseResponse create(CourseRequestDTO courseDTO) throws DataNotFoundException;

    /**
     * Function: chuc nang lay ra tat ca va loc theo yeu cau cac khoa hoc
     *
     * @param keyword     key word tim kiem theo tieu de
     * @param categoryId  id danh muc cua khoa hoc
     * @param languageId  id ngon ngu duoc su dung trong khoa hoc
     * @param languageId  id cua nguoi huong dan khoa hoc
     * @param pageRequest {@link PageRequest} dung de phan trang
     * @return Page<CourseResponse> {@link CourseResponse}  tra ve du lieu thanh cong va da phan trang
     * @throws DataNotFoundException nem ma code 404 khi khong tim thay du lieu, hay sai duong dan)
     */
    public Page<CourseResponse> getAllCourses(String keyword, Long categoryId, Long languageId, Long instructorId, PageRequest pageRequest) throws DataNotFoundException;

    /**
     * Function: De upload file len cloudinary
     *
     * @param courseId      course id de upload video cho khoa hoc theo id nay
     * @param multipartFile file video de upload(file dung dinh dang, max size 100MB, chi upload 1 file duy nhat)
     * @return tra re Map<String, Object> result chua cac thong tin ve 1 file khi duoc upload tren cloudinary
     * @throws IOException nem ex khi loi lien quan den file (khi file khong doc duoc hay khong tim thay file)
     * @throws DataNotFoundException nem ex ma loi 404 khi tim khoa hoc theo courseId khong tim thay
     * @throws MaxUploadSizeExceededException nem ex ma loi 413 khi file lon hon size mac dinh (100MB)
     */
    public Map<String, Object> uploadFileToCloudinary(Long courseId, MultipartFile multipartFile) throws DataNotFoundException, IOException, MaxUploadSizeExceededException;
}
