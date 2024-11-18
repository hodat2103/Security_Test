package com.vsii.coursemanagement.dtos.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;

/**
 * DTO chua cac du lieu de chuyen doi cac du lieu tu entity
 * cac truong du lieu se duoc validate tuong ung voi dac ta yeu cau
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class CourseRequestDTO {

    @NotBlank(message = "Title must not be blank")
    private String title;

    @NotBlank(message = "Description must not be blank")
    private String description;

    @NotNull(message = "Price must not be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal price;


    @Min(value = 1, message = "Category ID must be a positive integer")
    @JsonProperty("category_id")
    private Long categoryId;

    @Min(value = 1, message = "Language ID must be a positive integer")
    @JsonProperty("language_id")
    private Long languageId;

    @Min(value = 1, message = "Instructor ID must be a positive integer")
    @JsonProperty("instructor_id")
    private Long instructorId;
}
