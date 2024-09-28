package com.project.closet.clothes;

import com.project.closet.member.MemberEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClothesRepository extends JpaRepository<ClothesEntity, Long> {

    List<ClothesEntity> findByCategorycodeAndMemberNo(Long categorycode, Long memberNo);


    @Query("SELECT c FROM ClothesEntity c JOIN FETCH c.categoryName WHERE c.memberNo = :memberNo ORDER BY c.categorycode ASC")
    List<ClothesEntity> findByMemberNoWithCategoryOrderByCategorycodeAsc(@Param("memberNo") Long memberNo);

    List<ClothesEntity> findByNameContainsAndMemberNo(String name, Long memberNoMethod);
}
