package com.vsii.coursemanagement.dtos.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseVideoRequestDTO {

    @Min(value = 1, message = "Course ID must be a positive integer")
    @JsonProperty("course_id")
    private Long courseId;

    @NotBlank(message = "Video url must not be blank")
    @JsonProperty("video_url")
    private String videoUrl;

}
