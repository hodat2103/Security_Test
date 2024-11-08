package com.vsii.coursemanagement.controllers;


import com.vsii.coursemanagement.dtos.response.ResponseError;
import com.vsii.coursemanagement.dtos.response.ResponseSuccess;
import com.vsii.coursemanagement.services.IInstructorService;
import com.vsii.coursemanagement.services.ILanguageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * LanguageController bo dieu khien de xu ly cac van de lien quan den api cua quan ly ngon ngu su dung trong khoa hoc.
 * Cung cap endpoint de truy xuat hay them, sua, xoa lien quan den ngon ngu su dung trong khoa hoc.
 * {@link ILanguageService} dung trong viec xu ly cac logic lien quan den ngon ngu su dung trong khoa hoc.
 * response (http code, message, data) tuong ung voi tung ket qua phu hop
 */

@RestController
@RequestMapping("${api.prefix}/languages")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Language Controller")

public class LanguageController {

    private final ILanguageService languageService;

    @Operation(summary = "Get list of languages", description = "Send a request via this API to get language list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved languages"),
            @ApiResponse(responseCode = "204", description = "No instructors found"),
            @ApiResponse(responseCode = "404", description = "URL not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("")
    public ResponseEntity<?> getAllLanguages(){
        try {
            ResponseSuccess response = languageService.getAllLanguages();

            if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
                // log ra va tra ve 204 neu du lieu danh sach rong
                log.warn("No languages found (status 204). Returning empty response.");
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
            }

            // tra ve 200 ok neu du lieu danh sach co san
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            // log ra exception va tra ve loi 500 phia server
            log.error("Error retrieving languges: {}", e.getMessage(), e);
            ResponseError errorResponse = new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Server error occurred while fetching categories.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
