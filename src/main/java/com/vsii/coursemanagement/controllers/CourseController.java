package com.vsii.coursemanagement.controllers;

import com.vsii.coursemanagement.configurations.Translator;
import com.vsii.coursemanagement.dtos.request.CourseRequestDTO;
import com.vsii.coursemanagement.dtos.response.ResponseSuccess;
import com.vsii.coursemanagement.dtos.response.data.CourseResponse;
import com.vsii.coursemanagement.exceptions.DataNotFoundException;
import com.vsii.coursemanagement.exceptions.InvalidParamException;
import com.vsii.coursemanagement.services.ICourseService;
import com.vsii.coursemanagement.services.ICourseVideoService;
import com.vsii.coursemanagement.utils.MessageKey;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * CourseController bo dieu khien de xu ly cac van de lien quan den api cua quan ly khoa hoc.
 * Cung cap endpoint de truy xuat hay them, sua, xoa lien quan den khoa hoc.
 * {@link ICourseService} dung trong viec xu ly cac logic lien quan den khoa hoc.
 * {@link ICourseVideoService} dung trong viec xu ly cac logic lien quan den video cua khoa hoc.
 * response (http code, message or(data)) tuong ung voi tung ket qua phu hop
 */

@RestController
@RequestMapping("${api.prefix}/courses")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Course Controller", description = "APIs for managing course")
public class CourseController {

    private final ICourseService courseService;
    private final ICourseVideoService courseVideoService;

    /**
     * Dung de tao 1 khoa hoc moi, tiep nhan bo du lieu tu client va tra ve phan hoi tuong ung
     *
     * @param courseRequestDTO bo du lieu can de tao 1 khoa hoc .
     * @return ResponseEntity<ResponseSuccess> response (httpcode, message or (data)) theo ket qua tuong ung.
     */

    @Operation(summary = "Create a new course", description = "Send a request via this API to add a new course")
    @PostMapping("")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved categories"),
            @ApiResponse(responseCode = "404", description = "URL not found"),
            @ApiResponse(responseCode = "400", description = "Client side error"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> create(@RequestBody @Valid CourseRequestDTO courseRequestDTO) {


        CourseResponse response = null;
        try {
            response = courseService.create(courseRequestDTO);
            // tra ve phan hoi 204 khi tao moi 1 khoa học thanh cong
            return ResponseEntity.ok(response);

        } catch (DataNotFoundException e) {
            log.error("Error 404 - Url not found or data failed: {}", e.getMessage());
            throw new RuntimeException(e);
        } catch (InvalidParamException e) {
            log.error("Error 400 - invalid param exception for courseId {}: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Phuong thuc de upload video len cloudinary cho khoa học tuong ung qua course id
     * Dieu kien upload video: Chi 1 video duoc upload
     * Kich thuoc video duoi 5MB
     * Video upload phai dung dinh dang mp4,mov...
     *
     * @param courseId      id cua khoa hoc tuong ung de upload video.
     * @param multipartFile tep video can upload len cloudinary
     * @return tra ve ok thanh cong va message, neu that bai nem ra exception.
     */
    @Operation(summary = "Upload video to cloudinary",
            description = "Send a request via this API to upload video of the course to cloudinary" +
            "Conditions: just only video file - \n" +
                    "video size smaller then 20MB - \n" +
                    "Video file while upload must follow format standards example(mp4, mov,...)")
    @PostMapping(value = "/uploadVideo/{course_id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved video of course"),
            @ApiResponse(responseCode = "404", description = "Not found url of data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> uploadFile(@PathVariable(name = "course_id") @Min(1) int courseId,
                             @RequestPart("video") MultipartFile multipartFile) throws DataNotFoundException, IOException {

        try {
            // kiem tra course id co ton tai ?
            if (courseId <= 0) {
                log.warn("Course ID {} is invalid or not found", courseId);
                throw new DataNotFoundException("Not found URL or data");
            }

            // goi service de upload video
            Map<String, Object> uploadResult = courseService.uploadFileToCloudinary(courseId, multipartFile);
            log.info("Upload successful for courseId: {}", courseId);

            // tra ve 200 khi upload video thanh cong
            return ResponseEntity.ok(Translator.toLocale(MessageKey.UPLOAD_FILE_SUCCESSFULLY) + ": " + uploadResult.get("videoUrl"));

        } catch (DataNotFoundException e) {
            log.error("Error 404 - Course ID {} not found: {}", courseId, e.getMessage());
            throw e; // // nem ra de ControllerAdvice xu ly thanh loi 404

        } catch (IOException e) {
            log.error("Error 500 - IOException occurred while uploading video for courseId {}: {}", courseId, e.getMessage());
            throw new RuntimeException("File upload error: " + e.getMessage(), e);  // nem ra de ControllerAdvice xu ly thanh loi 500

        } catch (Exception e) {
            log.error("Error 500 - Unexpected exception for courseId {}: {}", courseId, e.getMessage());
            throw new RuntimeException("An unexpected error occurred", e);  // nem ra de ControllerAdvice xu ly thanh loi 500
        }

    }

    /**
     * Lay thong tin video theo id.
     * Phuong thuc nay se tra ve thong tin video cua khoa hoc can tim .
     *
     * @param courseId ID cua khoa hoc can lay thong tin video cua khoa hoc do.
     * @return Thông tin video hoặc null nếu không tìm thấy.
     */

    @Operation(summary = "Get a video course", description = "Retrieve a video course with course id")
    @GetMapping("/videos/{course_id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved video of course"),
            @ApiResponse(responseCode = "404", description = "Not found url of data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> getById(@PathVariable("course_id") int courseId) {
        try {

            // lay thong tin video khoa hoc theo ID
            String videoUrl = courseVideoService.getByCourseId(courseId);

            // kiem tra neu video url khong ton tai
            if (videoUrl == null) {

                log.error("Not found video with course ID: " + courseId);
                throw new DataNotFoundException("Video not found for course ID: " + courseId);

            }
            // tra ve ma code 200
            return ResponseEntity.ok(videoUrl);

        } catch (DataNotFoundException e) {
            log.error("Data not found: {}", e.getMessage(), e);
            throw new RuntimeException();
        } catch (Exception e) {
            // Log loi nem ngoai le chung GlobalException xu ly
            log.error("An error occurred while retrieving video information: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Phuong thuc nay de lay ra danh sach khoa hoc theo cac truong du lieu va duoc phan trang (gioi han trang va trang hien tai).
     * cach hoat dong cua phuong thuc nay la theo kieu truy van dong cho truong du lieu truyen vao (cac truong nay co the de trong)
     *
     * @param keyword    tu khoa tim kiem (cho tieu de cua khoa hoc).
     * @param categoryId id cua danh muc
     * @param languageId id cua ngon ngu duoc su dung trong khoa hoc.
     * @param page       so trang ma truy van duoc mac dinh la 0
     * @param size       kich thuoc gioi han cho moi trang (mac dinh la 5).
     *                   {@BidingResult}   dung de kiem tra cac loi tu validation
     * @return ResponseEntity<ResponseSuccess> response (http code, massage, data) theo ket qua phu hop.
     */

    @Operation(summary = "Get all courses", description = "Retrieve all courses with optional filters and pagination")
    @GetMapping("")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved categories"),
            @ApiResponse(responseCode = "204", description = "No courses found"),
            @ApiResponse(responseCode = "400", description = "Client side error"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> getAllCourses(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "categoryId", required = false) @Min(1) Integer categoryId,
            @RequestParam(value = "languageId", required = false) @Min(1) Integer languageId,
            @RequestParam(value = "instructorId", required = false) @Min(1) Integer instructorId,
            @RequestParam(value = "page", defaultValue = "0") @Min(0) int page,
            @RequestParam(value = "size", defaultValue = "5") @Min(1) @Max(15) int size,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {

            // lay tat ca cac loi tu bindingResult chuyen thanh dang chuoi field: message roi merger lai
            String errorMessage = bindingResult.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                    .collect(Collectors.joining(", "));

            //log va tra ve loi 400 du lieu tu phia client
            log.error("Error validate data from client: {}", errorMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Validation failed: " + errorMessage);
        }

        try {
            PageRequest pageRequest = PageRequest.of(page, size);

            ResponseSuccess response = courseService.getAllCourses(keyword, categoryId, languageId,instructorId, pageRequest);

            if (response.getStatusCode().value() == HttpStatus.NO_CONTENT.value()) {
                // log ra va tra ve 204 neu du lieu danh sach rong
                log.warn("No courses found (status 204), returning empty response.");
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
            }

            // tra ve pan hoi 200 OK khi loc duoc thong tin theo yeu cau
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // log loi ra va tra ve loi 500 phia server
            log.error("Error retrieving courses: {}", e.getMessage(), e);
            throw e;
        }
    }
}
