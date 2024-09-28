package com.project.closet.clothescategory;

import com.project.closet.clothes.ClothesEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
@Entity
@Setter
@Getter
@Table(name = "clothesCategory")
public class ClothesCategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categorycode;

    @Column(nullable = false)
    private String CategoryName;

    @ToString.Exclude
    @OneToMany(mappedBy = "categoryName")
    private List<ClothesEntity> clothes = new ArrayList<>();
}
