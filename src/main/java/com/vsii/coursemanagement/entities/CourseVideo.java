package com.vsii.coursemanagement.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "course_video")
@Builder
public class CourseVideo {
    /**
     * id la khoa chinh va tu dong tang
     * id duy nhat
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "video_id")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(name = "video_url")
    private String videoUrl; // url video dung de truy cap toi cloudinary


}
