package com.vsii.coursemanagement.dtos.response.data;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.vsii.coursemanagement.entities.Course;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseResponse extends BaseResponse{
    private Long id;

    private String title;

    @JsonProperty("instructor_id")
    private Long instructorId;

    private String description;

    private BigDecimal price;

    @JsonProperty("category_id")
    private Long categoryId;

    @JsonProperty("language_id")
    private Long languageId;


    public static CourseResponse fromCourse(Course course){
        CourseResponse courseResponse = CourseResponse.builder()
                .id(course.getId())
                .title(course.getTitle())
                .instructorId(course.getInstructor().getId())
                .description(course.getDescription())
                .price(course.getPrice())
                .categoryId(course.getCategory().getId())
                .languageId(course.getLanguage().getId())
                .build();
        courseResponse.setCreatedAt(course.getCreatedAt());
        courseResponse.setUpdatedAt(course.getUpdatedAt());

        return courseResponse;
    }
}

