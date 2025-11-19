package com.project.closet.member;

import com.project.closet.oauth.MemberRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;
    @Override
    public UserDetails loadUserByUsername(String memberid) throws UsernameNotFoundException {
        // username을 가진 유저를 찾아와서
        // return new User(유저아이디, 비번, 권한) 해주세요
        var result =  memberRepository.findByMemberid(memberid);
        if(result == null){
            throw new UsernameNotFoundException("그런아이디 없음");
        }
        var member = result;
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("일반유저"));

        var a = new CustomUser(member.getMemberid(), member.getMemberpassword(), authorities);
        a.membername = member.getMembername();
        a.memberno = member.getId();
        MemberRole b =  member.getRole();
        a.role = b.name();

        return a;
    }

}
