package com.example.projectboard.config;

import com.example.projectboard.dto.UserAccountDto;
import com.example.projectboard.dto.security.BoardPrincipal;
import com.example.projectboard.repository.UserAccountRepository;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebSecurity
@EnableWebMvc
public class SecurityConfig {

    // Spring Security 3.0 이상 버전
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http.authorizeHttpRequests(auth -> auth
                                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                                        .requestMatchers( // antMatchers(), mvcMatchers(), regexMatchers() 변경
                                    HttpMethod.GET, "/",
                                    "/articles",
                                    "/articles/search-hashtag"
                        ).permitAll().anyRequest().authenticated())
                        .formLogin(login -> login
                            .defaultSuccessUrl("/articles", true)
                            .permitAll());

                return http.build();

    }

    @Bean
    public UserDetailsService userDetailsService(UserAccountRepository userAccountRepository) {
        // loadUserByusername()
        return username -> userAccountRepository
                .findById(username)
                .map(UserAccountDto::from)
                .map(BoardPrincipal::from)
                .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다 - username: " + username));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
