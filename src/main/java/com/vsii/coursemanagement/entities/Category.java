package com.vsii.coursemanagement.entities;

import jakarta.persistence.*;
import lombok.*;

/**
 *  lop entity nay la dai dien cho du lieu category cua he thong
 *  Su dung cac annotation de toi uu code, su dung khi can den viec truy xuat hay xu ly du lieu
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "category")
@Builder
public class Category {
    /**
     * id la khoa chinh va tu dong tang
     * id duy nhat
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    // ten cua danh muc ex: (Backend, Frontend, Devops)
    @Column(name = "name")
    private String name;

    // mo ta chi tiet ve mot danh muc
    @Column(name = "description")
    private String description;
}
