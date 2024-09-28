package com.project.closet.member;

import com.project.closet.oauth.MemberRole;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@Setter
@Entity
@Table(name = "member")
@NoArgsConstructor
@AllArgsConstructor
public class MemberEntity {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String memberid;

    @Column
    private String memberpassword;

    @Column
    private String membername;

    // 회원 가입 날짜를 저장할 필드
    @Column(nullable = false, updatable = false)
    private String joinDate;

    // provider : google이 들어감
    private String provider;

    // providerId : 구굴 로그인 한 유저의 고유 ID가 들어감
    private String providerId;

    // 회원 가입 시 자동으로 현재 시간을 설정하는 메서드

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    private transient Integer no;


}
