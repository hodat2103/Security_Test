package com.vsii.coursemanagement.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
/**
 *  lop entity nay la dai dien cho du lieu course cua he thong
 *  Su dung cac annotation de toi uu code, su dung khi can den viec truy xuat hay xu ly du lieu
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "course")
@Builder
public class Course extends BaseEntity{
    /**
     * id la khoa chinh va tu dong tang
     * id duy nhat
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Long id;

    @Column(name = "title")
    private String title; // tieu de cua khoa hoc

    @Column(name = "description")
    private String description; // mo ta chi tiet ve khoa hoc

    @Column(name = "price")
    private BigDecimal price; // gia cua khoa hoc


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category; // danh muc bao gom nhu (Backend, Frontend, Devops,...)

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "language_id")
    private Language language; // ngon ngu duoc su dung trong khoa hoc (Java, Nodejs, Python,...)

    @ManyToOne(fetch = FetchType.EAGER) // join n -> 1
    @JoinColumn(name = "instructor_id")
    private Instructor instructor; // nguoi huong dan khoa hoc


}
