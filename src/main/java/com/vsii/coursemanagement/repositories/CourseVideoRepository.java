package com.vsii.coursemanagement.repositories;

import com.vsii.coursemanagement.entities.CourseVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseVideoRepository extends JpaRepository<CourseVideo, Long> {
   public CourseVideo getByCourseId(Long courseId);
}
