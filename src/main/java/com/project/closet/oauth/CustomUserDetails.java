package com.project.closet.oauth;

import com.project.closet.member.MemberEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Arrays;
import java.util.Map;

public class CustomUserDetails extends User implements OAuth2User {
    private final MemberEntity member;
    private Map<String, Object> attributes;

    public CustomUserDetails(MemberEntity member, Map<String, Object> attributes) {
        // 소셜 로그인 사용자는 비밀번호가 없을 수 있으므로 임시 비밀번호 설정
        super(member.getMemberid(), (member.getMemberpassword() != null ? member.getMemberpassword() : "SOCIAL_USER_TEMP_PASSWORD"),
                Arrays.asList(new SimpleGrantedAuthority("일반유저")));
        this.member = member;
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return this.getUsername();
    }
}
