package com.vsii.coursemanagement.controllers;

import com.vsii.coursemanagement.components.Translator;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

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
            @ApiResponse(responseCode = "201", description = "Successfully retrieved categories"),
            @ApiResponse(responseCode = "404", description = "URL not found while not return data or url failed"),
            @ApiResponse(responseCode = "400", description = "Client side error while invalid  input data such as validated"),
            @ApiResponse(responseCode = "500", description = "Internal server error while not connect with database")})
    public ResponseEntity<?> create(@RequestBody @Valid CourseRequestDTO courseRequestDTO) throws MethodArgumentNotValidException, DataNotFoundException {


        CourseResponse courseResponse = courseService.create(courseRequestDTO);

        ResponseSuccess responseSuccess = new ResponseSuccess(HttpStatus.CREATED, Translator.toLocale(MessageKey.COURSE_CREATE_SUCCESSFULLY), courseResponse);
        // tra ve phan hoi 204 khi tao moi 1 khoa học thanh cong
        return ResponseEntity.ok(responseSuccess);


    }

    /**
     * Phuong thuc de upload video len cloudinary cho khoa học tuong ung qua course id
     * Dieu kien upload video: Chi 1 video duoc upload
     * Kich thuoc video duoi 100MB
     * Video upload phai dung dinh dang mp4,mov...
     *
     * @param courseId      id cua khoa hoc tuong ung de upload video.
     * @param multipartFile tep video can upload len cloudinary
     * @return tra ve ok thanh cong va message, neu that bai nem ra exception.
     */
    @Operation(summary = "Upload video to cloudinary", description = "Send a request via this API to upload video of the course to cloudinary" + "Conditions: just only video file - \n" + "video size smaller then 20MB - \n" + "Video file while upload must follow format standards example(mp4, mov,...)")
    @PostMapping(value = "/uploadVideo/{course_id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved video of course"),
            @ApiResponse(responseCode = "404", description = "Not found url of data"),
            @ApiResponse(responseCode = "500", description = "Internal server error while not connect with database and can't access to cloudinary")})
    public ResponseEntity<?> uploadFile(@PathVariable(name = "course_id") @Min(1) Long courseId, @RequestPart("video") MultipartFile multipartFile) throws DataNotFoundException, IOException, InvalidParamException {


        // kiem tra course id co ton tai ?

        if (courseId <= 0) {
            throw new DataNotFoundException("Not found URL or data");
        }

        // goi service de upload video
        Map<String, Object> uploadResult = courseService.uploadFileToCloudinary(courseId, multipartFile);
        log.info("Upload successful for courseId: {}", courseId);

        ResponseSuccess responseSuccess = new ResponseSuccess(HttpStatus.OK, Translator.toLocale(MessageKey.UPLOAD_FILE_SUCCESSFULLY), uploadResult.get("videoUrl"));

        // tra ve 200 khi upload video thanh cong
        return ResponseEntity.ok(responseSuccess);
    }

    /**
     * Lay thong tin video theo id.
     * Phuong thuc nay se tra ve thong tin video cua khoa hoc can tim .
     *
     * @param courseId ID cua khoa hoc can lay thong tin video cua khoa hoc do.
     * @return Thông tin video hoặc null nếu không tìm thấy.
     * @throw Exception khi khong tim thay url tren cloudinary
     */

    @Operation(summary = "Get a video course", description = "Retrieve a video course with course id")
    @GetMapping("/videos/{course_id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved video of course"),
            @ApiResponse(responseCode = "404", description = "Not found url of data"),
            @ApiResponse(responseCode = "500", description = "Internal server error while not connect with database, can't access to cloudinary ")})
    public ResponseEntity<?> getById(@PathVariable("course_id") Long courseId) throws Exception {

        // lay thong tin video khoa hoc theo ID
        String videoUrl = courseVideoService.getByCourseId(courseId);

        // kiem tra neu video url khong ton tai
        if (videoUrl == null) {
            throw new DataNotFoundException("Video not found  course ID: " + courseId);
        }
        // tra ve ma code 200
        return ResponseEntity.ok(videoUrl);
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
     *                   {@BindingResult} dung de kiem tra cac loi tu validation
     * @return ResponseEntity<ResponseSuccess> response (http code, massage, data) theo ket qua phu hop.
     * @throw DataNotFoundException khi sai url hoac khong tra ve du lieu tu database
     */

    @Operation(summary = "Get all courses", description = "Retrieve all courses with optional filters and pagination")
    @GetMapping("")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved categories"),
            @ApiResponse(responseCode = "204", description = "No courses found"),
            @ApiResponse(responseCode = "400", description = "Client side error while client enter invalid data such as the format"),
            @ApiResponse(responseCode = "500", description = "Internal server error while not connect with database")
    })
    public ResponseEntity<?> getAllCourses(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "categoryId", required = false) @Min(1) Long categoryId,
            @RequestParam(value = "languageId", required = false) @Min(1) Long languageId,
            @RequestParam(value = "instructorId", required = false) @Min(1) Long instructorId,
            @RequestParam(value = "page", defaultValue = "0") @Min(0) int page,
            @RequestParam(value = "size", defaultValue = "5") @Min(1) @Max(15) int size) throws DataNotFoundException {

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<CourseResponse> courseResponses = courseService.getAllCourses(keyword, categoryId, languageId, instructorId, pageRequest);
        int totalPage = courseResponses.getTotalPages();
        if(totalPage == 0){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        ResponseSuccess responseSuccess = new ResponseSuccess(HttpStatus.OK,
                Translator.toLocale(MessageKey.COURSE_RETRIEVE_SUCCESSFULLY ,totalPage), courseResponses);

        // Return 200 OK when courses are successfully retrieved
        return ResponseEntity.ok(responseSuccess);
    }
}
