package com.project.closet.member;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    MemberEntity findByMemberid(String memberid);
    @Modifying
    @Transactional
    @Query("UPDATE MemberEntity m SET m.membername = :membername WHERE m.id = :id")
    void updateMemberName(@Param("id") Long id, @Param("membername") String membername);

    @Modifying
    @Transactional
    @Query("UPDATE MemberEntity m SET m.memberpassword = :memberpassword WHERE m.id = :id")
    void updateMemberPassword(@Param("id") Long id, @Param("memberpassword") String memberpassword);


    List<MemberEntity> findByMembernameContains(String memberName);
}
