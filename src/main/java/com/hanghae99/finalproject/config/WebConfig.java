package com.hanghae99.finalproject.config;

import com.hanghae99.finalproject.interceptor.JwtTokenInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.*;
import org.springframework.web.servlet.config.annotation.*;

import static com.hanghae99.finalproject.interceptor.JwtTokenInterceptor.JWT_HEADER_KEY;

@RequiredArgsConstructor
@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    private final String[] JWT_INTERCEPTOR_URI = {
            "/user/signup",
            "/user/emailDupCheck/**",
            "/user/nameDupCheck/**",
            "/user/login",
            "/user/social",
            "/image/og/**"
    };
    public final static String SOCIAL_HEADER_KEY = "Credential";

    private final JwtTokenInterceptor jwtTokenInterceptor;

    @Bean
    public BCryptPasswordEncoder encodePassword() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:///home/ubuntu/upload/");
    }

    // CORS 허용 적용
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:3000");
        configuration.addAllowedHeader("Content-Type");
        configuration.addAllowedHeader("Custom-Header");
        configuration.addAllowedMethod(HttpMethod.POST);
        configuration.addAllowedMethod(HttpMethod.GET);
        configuration.addAllowedMethod(HttpMethod.POST);
        configuration.addAllowedMethod(HttpMethod.PUT);
        configuration.addAllowedMethod(HttpMethod.OPTIONS);
        configuration.addAllowedMethod(HttpMethod.DELETE);
        configuration.setAllowCredentials(true);
        configuration.addAllowedHeader(JWT_HEADER_KEY);
        configuration.addAllowedHeader(SOCIAL_HEADER_KEY);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().frameOptions().sameOrigin()
                    .and()
                .cors().configurationSource(corsConfigurationSource())
                    .and()
                .authorizeHttpRequests(auth -> {
                            try {
                                auth.anyRequest().permitAll();
                            } catch (Exception e) {
                                throw new RuntimeException(e.getMessage());
                            }
                        }
                );
        return http.build();
    }

    //토큰을 받아야 하는 서비스 설정
    public void addInterceptors(InterceptorRegistry registry) {
        registry
                .addInterceptor(jwtTokenInterceptor) //로그인이 필요한 서비스 요청시 Interceptor가 그 요청을 가로챔
                .excludePathPatterns(JWT_INTERCEPTOR_URI);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}
