package org.vsa.server.member;

import org.vsa.server.member.repository.MemberRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;
    private final AuthenticationConfiguration authenticationConfiguration;

    public SecurityConfig(JwtUtil jwtUtil,AuthenticationConfiguration authenticationConfiguration, MemberRepository memberRepository) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtUtil = jwtUtil;
        this.memberRepository = memberRepository;
    }

    private static final String[] AUTH_WHITELIST = {
            "/v3/api-docs/**",   // Swagger 3 documentation endpoint
            "/swagger-ui/**",     // Swagger UI endpoint
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/webjars/**",        // Webjars used by Swagger UI
            "/api/oauth",         // 카카오 로그인 엔드포인트
            "/api/logout",  // 로그아웃 엔드포인트
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화
                .authorizeRequests(auth -> auth
                        .requestMatchers(CorsUtils::isPreFlightRequest).permitAll() // CORS 프리플라이트 요청 허용
                        .requestMatchers("/api/inquire","/api/view").hasAnyRole("ADMIN", "STANDARD", "PREMIUM")
                        .requestMatchers(AUTH_WHITELIST).permitAll()// 인증이 필요 없는 요청들
                        .anyRequest().authenticated() // 그 외 요청은 인증 필요
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // JWT 사용 시 STATELESS 설정
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/api/logout")) // 로그아웃 URL 지정
                        .logoutSuccessUrl("/") // 로그아웃 성공 후 리다이렉트 URL
                        .deleteCookies("JSESSIONID")) // 로그아웃 시 세션 쿠키 삭제
                .addFilterBefore(new JWTFilter(jwtUtil, memberRepository), UsernamePasswordAuthenticationFilter.class) // JWT Filter 추가
                .cors(cors -> cors.configurationSource(corsConfigurationSource())); // CORS 설정

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.addAllowedOriginPattern("*"); // 개발 중 모든 도메인 허용
        configuration.addAllowedHeader("*"); // 모든 헤더 허용
        configuration.addAllowedMethod("*"); // 모든 HTTP 메서드 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
