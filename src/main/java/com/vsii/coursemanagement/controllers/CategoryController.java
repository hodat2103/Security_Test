package com.vsii.coursemanagement.controllers;

import com.vsii.coursemanagement.components.Translator;
import com.vsii.coursemanagement.dtos.response.ResponseSuccess;
import com.vsii.coursemanagement.entities.Category;
import com.vsii.coursemanagement.exceptions.DataNotFoundException;
import com.vsii.coursemanagement.services.ICategoryService;
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
 * CategoryController bo dieu khien de xu ly cac van de lien quan den api cua quan ly danh muc.
 * Cung cap endpoint de truy xuat hay them, sua, xoa lien quan den danh muc.
 * {@link ICategoryService} dung trong viec xu ly cac logic lien quan den danh muc.
 * response (http code, message, data) tuong ung voi tung ket qua phu hop
 */

@RestController
@RequestMapping("${api.prefix}/categories")
@RequiredArgsConstructor
@Tag(name = "Category Controller", description = "APIs for managing categories")
@Slf4j
public class CategoryController {

    private final ICategoryService categoryService;

    @Operation(summary = "Get list of categories", description = "Fetches a list of all available categories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved categories"),
            @ApiResponse(responseCode = "204", description = "No categories found"),
            @ApiResponse(responseCode = "404", description = "URL not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<?> getAllCategories() throws DataNotFoundException {
        List<Category> categories = categoryService.getAllCategories();

        ResponseSuccess response = new ResponseSuccess(
                HttpStatus.OK,
                Translator.toLocale(MessageKey.CATEGORIES_RETRIEVE_SUCCESSFULLY),
                categories
        );

        // tra ve 200 ok neu du lieu danh sach co san
        return ResponseEntity.ok(response);


    }
}
