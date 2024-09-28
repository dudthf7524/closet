package com.project.closet.oauth;

import com.project.closet.member.MemberEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@RequiredArgsConstructor
public class CustomOauth2UserDetails implements UserDetails, OAuth2User {

    private final MemberEntity member;
    private Map<String, Object> attributes;

    public CustomOauth2UserDetails(MemberEntity member, Map<String, Object> attributes) {

        this.member = member;
        this.attributes = attributes;
    }

    public Long getId() {
        return member.getId();  // Member 엔티티의 id 필드 반환
    }

    public MemberRole getRole(){
        
        return member.getRole();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return member.getRole().name();
            }
        });

        return collection;
    }



    @Override
    public String getPassword() {
        return member.getMemberpassword();
    }

    @Override
    public String getUsername() {
        return member.getMemberid();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
