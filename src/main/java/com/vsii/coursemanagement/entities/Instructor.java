package com.vsii.coursemanagement.entities;

import jakarta.persistence.*;
import lombok.*;

/**
 *  Lop entity nay la dai dien cho du lieu instructor cua he thong
 *  Su dung cac annotation de toi uu code, su dung khi can den viec truy xuat hay xu ly du lieu
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "instructor")
@Builder
public class Instructor {
    /**
     * id la khoa chinh va tu dong tang
     * id duy nhat
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "instructor_id")
    private Long id;

    // ten cua nguoi huong dan khoa hoc
    @Column(name = "name")
    private String name;

    // email ca nhan cua nguoi huong dan
    @Column(name = "email")
    private String email;
}
