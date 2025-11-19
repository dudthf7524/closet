package com.project.closet.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity

public class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf((crsf) ->crsf.disable());

        http.authorizeHttpRequests((authorize) ->
                authorize.requestMatchers("/**").permitAll()
        );
        http.formLogin((formLogin) -> formLogin.loginPage("/member/login")
                .usernameParameter("memberid")
                .passwordParameter("memberpassword")
                .failureForwardUrl("/loginError")
                .defaultSuccessUrl("/")

        );
        http.logout(logout -> logout.logoutUrl("/member/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")  // 쿠키 삭제
                .permitAll()
                .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout", "GET")) // GET 방식 로그아웃 허용
        );
        http.oauth2Login((auth) -> auth.loginPage("/oauth-login/login")
                        .defaultSuccessUrl("/")
                        .failureUrl("/oauth-login/login")
                        .permitAll());



        return http.build();
    }


}
