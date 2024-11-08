package com.vsii.coursemanagement.services.implement;

import com.cloudinary.utils.ObjectUtils;
import com.vsii.coursemanagement.configurations.CloudinaryConfig;
import com.vsii.coursemanagement.configurations.Translator;
import com.vsii.coursemanagement.dtos.request.CourseRequestDTO;
import com.vsii.coursemanagement.dtos.response.ResponseSuccess;
import com.vsii.coursemanagement.dtos.response.data.CourseResponse;
import com.vsii.coursemanagement.entities.*;
import com.vsii.coursemanagement.exceptions.DataNotFoundException;
import com.vsii.coursemanagement.exceptions.InvalidParamException;
import com.vsii.coursemanagement.repositories.*;
import com.vsii.coursemanagement.services.ICourseService;
import com.vsii.coursemanagement.utils.ConstantKey;
import com.vsii.coursemanagement.utils.MessageKey;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

/**
 * Service tiep nhan cac cong viec tu IService nay xu ly cac logic lien quan den khoa hoc
 */
@Service
@RequiredArgsConstructor
public class CourseService implements ICourseService {

    private final CourseRepository courseRepository;
    private final CourseVideoRepository courseVideoRepository;
    private final CategoryRepository categoryRepository;
    private final LanguageRepository languageRepository;
    private final InstructorRepository instructorRepository;
    private final CloudinaryConfig cloudinaryConfig;


    /**
     * @param courseDTO bao gom chi tiet thong tin cua khoa hoc
     * @return
     * @throws DataNotFoundException (category, instructor, language not found in the database)
     * @throws InvalidParamException (khi cac param truyen vao khong hop le khi validate trong dto)
     */
    @Override
    public CourseResponse create(CourseRequestDTO courseDTO) throws DataNotFoundException, InvalidParamException {


            // kiem tra su ton tai cua category theo category id
            Category existsCategory = categoryRepository.findById(courseDTO.getCategoryId())
                    .orElseThrow(() -> new DataNotFoundException("Cannot find category with id: " + courseDTO.getCategoryId()));

            // kiem tra su ton tai cua language theo language id
            Language existsLanguage = languageRepository.findById(courseDTO.getLanguageId())
                    .orElseThrow(() -> new DataNotFoundException("Cannot find language with id: " + courseDTO.getLanguageId()));

            // kiem tra su ton tai cua instructor theo instructor id
            Instructor existsInstructor = instructorRepository.findById(courseDTO.getInstructorId())
                    .orElseThrow(() -> new DataNotFoundException("Cannot find instructor with id: " + courseDTO.getInstructorId()));

            // Tao mot khoa hoc moi

            // Call the stored procedure to insert the course and return the course ID
            Integer courseId = courseRepository.insertCourse(
                    courseDTO.getTitle(),
                    courseDTO.getDescription(),
                    courseDTO.getPrice(),
                    courseDTO.getCategoryId(),
                    courseDTO.getLanguageId(),
                    courseDTO.getInstructorId()
            );

            // lay du lieu khoa hoc moi tao
            Course createdCourse = courseRepository.findById(courseId)
                    .orElseThrow(() -> new RuntimeException("Course not found"));

            // chuyen sang DTO
            CourseResponse courseResponse = CourseResponse.fromCourse(createdCourse);
            return courseResponse;
    }

    /**
     *
     * @param videoFile file video dang MultipartFile
     * @throws InvalidParamException
     */
    private void validateVideoFile(MultipartFile videoFile) throws InvalidParamException {

        if (videoFile == null || videoFile.isEmpty()) {
            throw new InvalidParamException("You must upload exactly one non-empty video file.");
        }
        if (videoFile.getSize() > ConstantKey.MAX_FILE_SIZE_MB) {
            throw new InvalidParamException("File size exceeds 100MB limit.");
        }
        if (!ConstantKey.VALID_CONTENT_TYPES.contains(videoFile.getContentType())) {
            throw new InvalidParamException("Invalid file type. Only video files are accepted (e.g., mp4, mpeg, avi, mov).");
        }
    }

    /**
     *
     * @param courseId   id course tuong ung voi video cua khoa hoc do
     * @param videoFile file video de upload len cloudinary
     * @return
     * @throws IOException
     */
    @Override
    public Map<String, Object> uploadFileToCloudinary(Integer courseId, MultipartFile videoFile) throws InvalidParamException, DataNotFoundException, IOException {
        Map<String, Object> uploadResult = new HashMap<>();

        try {
            // Goi ham validate de ktra file truoc khi upload

            validateVideoFile(videoFile);
            // kiem tra su ton tai cua course
            Optional<Course> existsCourse = courseRepository.findById(courseId);
            if (existsCourse.get() == null) {
                throw new DataNotFoundException("Cannot find course with id: " + courseId);
            }
            String fileNameUnique = videoFile.getOriginalFilename() +"_" +UUID.randomUUID().toString();
            // upload video len cloudinary
            Map result = cloudinaryConfig.cloudinary().uploader().upload(videoFile.getBytes(),
                    ObjectUtils.asMap(
                            "resource_type", "video",
                            "public_id",fileNameUnique
                    ));

            // get video url save to db
            String videoUrl = result.get("secure_url").toString();
            CourseVideo courseVideo = CourseVideo.builder()
                    .course(existsCourse.get())
                    .videoUrl(fileNameUnique)
                    .build();
            courseVideoRepository.save(courseVideo);

            uploadResult.put("videoUrl", videoUrl);
            uploadResult.put("message", "Video uploaded successfully");
            return uploadResult;
        } catch (InvalidParamException e) {
            throw e;
        } catch (DataNotFoundException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        }


    }

    /**
     *
     * @param keyword     keyword theo tieu de cua khoa hoc can tim
     * @param fieldId     The ID of the field to filter courses by.
     * @param languageId  The ID of the language to filter courses by.
     * @param pageRequest The PageRequest object for pagination information.
     * @return
     */
    @Override
    public ResponseSuccess getAllCourses(String keyword, Integer fieldId, Integer languageId, Integer instructorId, PageRequest pageRequest) {
        try {
            Page<Course> coursePage = courseRepository.searchCourses(fieldId, languageId,instructorId, keyword, pageRequest);
            Page<CourseResponse> courses = coursePage.map(CourseResponse::fromCourse);
            if (courses.isEmpty()) {
                // tra ve voi ma 204 khong co du lieu
                return new ResponseSuccess(HttpStatus.NO_CONTENT, Translator.toLocale(MessageKey.NO_COURSES_FOUND));
            }
            // tra ve trang thai 200 khi lay tat ca hay loc thanh cong du lieu theo yeu cau
            return new ResponseSuccess(HttpStatus.OK, "Courses retrieved successfully", courses);
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve courses: " + e.getMessage(), e);
        }
    }


}
