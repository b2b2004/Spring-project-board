package com.example.projectboard.config;

import com.example.projectboard.dto.security.BoardPrincipal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@EnableJpaAuditing
@Configuration
public class JpaConfig {

    @Bean
    public AuditorAware<String> auditorAware(){

        return () -> Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication) // Authentication정보를 불러옴
                .filter(Authentication::isAuthenticated)// 인증 됬는지 확인
                .map(Authentication::getPrincipal) // getPrincipal를 불러옴
                .map(BoardPrincipal.class::cast) // 만든 BoardPrincipal type 캐스팅
                .map(BoardPrincipal::getUsername);
   }
}
