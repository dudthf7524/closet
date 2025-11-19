package com.project.closet.clothes;

import com.project.closet.clothescategory.ClothesCategoryEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "clothes")
public class ClothesEntity {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private Integer price;

    @Column
    private String purchaseplace;

    @Column
    private String buydate;

    @Column
    private Long categorycode;

    @Column
    private String clothesfile;

    @Column
    private Long memberNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categorycode", referencedColumnName = "categorycode", insertable = false, updatable = false)
    private ClothesCategoryEntity categoryName;

    private transient Integer no;
    private transient String commaprice;
}
