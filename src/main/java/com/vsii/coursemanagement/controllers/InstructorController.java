package com.vsii.coursemanagement.controllers;

import com.vsii.coursemanagement.components.Translator;
import com.vsii.coursemanagement.dtos.response.ResponseSuccess;
import com.vsii.coursemanagement.entities.Instructor;
import com.vsii.coursemanagement.exceptions.DataNotFoundException;
import com.vsii.coursemanagement.services.IInstructorService;
import com.vsii.coursemanagement.utils.MessageKey;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * InstructorController bo dieu khien de xu ly cac van de lien quan den api cua quan ly nguoi huong dan.
 * Cung cap endpoint de truy xuat hay them, sua, xoa lien quan den nguoi huong dan.
 * {@link IInstructorService} dung trong viec xu ly cac logic lien quan den nguoi huong dan.
 * response (http code, message, data) tuong ung voi tung ket qua phu hop
 */

@RestController
@RequestMapping("${api.prefix}instructors")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Instructor Controller")

public class InstructorController {
    private final IInstructorService instructorService;

    @Operation(summary = "Get list of instructors",
            description = "Send a request via this API to get instructor list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved instructors"),
            @ApiResponse(responseCode = "204", description = "No instructors found"),
            @ApiResponse(responseCode = "404", description = "URL not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("")
    public ResponseEntity<?> getAllCategories() throws DataNotFoundException {

        List<Instructor> instructors = instructorService.getAllInstructors();

        ResponseSuccess response = new ResponseSuccess(
                HttpStatus.OK,
                Translator.toLocale(MessageKey.INSTRUCTORS_RETRIEVE_SUCCESSFULLY),
                instructors
        );

        // tra ve 200 ok neu du lieu danh sach co san
        return ResponseEntity.ok(response);

    }
}
