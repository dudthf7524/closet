package com.project.closet.member;

import com.project.closet.oauth.MemberRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CustomUser extends User {

    public Long memberno;
    public String membername;
    public String role;
    public CustomUser(
            String memberid,
            String memberpassword,
            Collection<? extends GrantedAuthority> authorities
    ) {
        super(memberid, memberpassword, authorities);
    }

}
