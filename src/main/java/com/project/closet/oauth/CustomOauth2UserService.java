package com.project.closet.oauth;

import com.project.closet.member.MemberEntity;
import com.project.closet.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOauth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("getAttributes : {}",oAuth2User.getAttributes());

        String provider = userRequest.getClientRegistration().getRegistrationId();

        OAuth2UserInfo oAuth2UserInfo = null;

        // 뒤에 진행할 다른 소셜 서비스 로그인을 위해 구분 => 구글
        if(provider.equals("naver")){
            log.info("네이버 로그인");
            oAuth2UserInfo = new NaverUserDetails(oAuth2User.getAttributes());

        }else if (provider.equals("kakao")) {
            log.info("카카오 로그인");
            oAuth2UserInfo = new KakaoUserDetails(oAuth2User.getAttributes());
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = LocalDateTime.now().format(formatter);
        String providerId = oAuth2UserInfo.getProviderId();
        String email = oAuth2UserInfo.getEmail();
        String loginId = provider + "_" + providerId;
        String name = oAuth2UserInfo.getName();

        MemberEntity findMember = memberRepository.findByMemberid(loginId);

        MemberEntity member;

        if (findMember == null) {
            member = MemberEntity.builder()
                    .memberid(loginId)
                    .membername(name)
                    .provider(provider)
                    .providerId(providerId)
                    .role(MemberRole.USER)
                    .joinDate(formattedDate)
                    .build();
            memberRepository.save(member);
        } else{
            member = findMember;
        }

        return new CustomOauth2UserDetails(member, oAuth2User.getAttributes());
    }
}
