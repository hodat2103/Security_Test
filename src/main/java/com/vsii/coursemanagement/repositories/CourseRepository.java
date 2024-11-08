package com.vsii.coursemanagement.repositories;

import com.vsii.coursemanagement.entities.Course;
import org.hibernate.result.Output;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {

    @Query("SELECT c FROM Course c WHERE " +
            "(:categoryId IS NULL OR :categoryId = 0 OR c.category.id = :categoryId)" +
            "AND (:languageId IS NULL OR :languageId = 0 OR c.language.id = :languageId)" +
            "AND (:instructorId IS NULL OR :instructorId = 0 OR c.instructor.id = :instructorId)" +
            "AND (:keyword IS NULL OR :keyword = '' OR c.title LIKE %:keyword%)")
    Page<Course> searchCourses(
            @Param("categoryId") Integer categoryId,
            @Param("languageId") Integer languageId,
            @Param("instructorId") Integer instructorId,
            @Param("keyword") String keyword, Pageable pageable
    );


    @Procedure(procedureName = "insert_course")
    Integer insertCourse(
            @Param("p_title") String title,
            @Param("p_description") String description,
            @Param("p_price") BigDecimal price,
            @Param("p_categoryId") int categoryId,
            @Param("p_languageId") int languageId,
            @Param("p_instructorId") int instructorId);

}
