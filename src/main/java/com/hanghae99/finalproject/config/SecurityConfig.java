package com.hanghae99.finalproject.config;

import com.hanghae99.finalproject.config.filter.LoginFilter;
import com.hanghae99.finalproject.config.handler.LoginSuccessHandler;
import com.hanghae99.finalproject.config.provider.FormLoginAuthProvider;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.*;

import static com.hanghae99.finalproject.config.handler.LoginSuccessHandler.AUTH_HEADER;

@Configuration
@EnableWebSecurity // 스프링 Security 지원을 가능하게 함
@EnableGlobalMethodSecurity(securedEnabled = true) // @Secured 어노테이션 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String[] AUTH_WHITELIST = {
            "/",
            "/h2-console/**",
            "/user/signup",
            "/user/idCheck/**",
            "/user/nicknameCheck/**",
            "/test"
    };

    @Bean
    public BCryptPasswordEncoder encodePassword() {
        return new BCryptPasswordEncoder();
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
        configuration.addExposedHeader(AUTH_HEADER);
        configuration.addAllowedHeader(AUTH_HEADER);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public LoginFilter formLoginFilter() throws Exception {
        LoginFilter loginFilter = new LoginFilter(authenticationManager());
        loginFilter.setFilterProcessesUrl("/user/login");
        loginFilter.setAuthenticationSuccessHandler(formLoginSuccessHandler());
        loginFilter.afterPropertiesSet();
        return loginFilter;
    }

    @Bean
    public LoginSuccessHandler formLoginSuccessHandler() {
        LoginSuccessHandler formLoginSuccessHandler = new LoginSuccessHandler();
        return formLoginSuccessHandler;
    }

    @Bean
    public FormLoginAuthProvider formLoginAuthProvider() {
        return new FormLoginAuthProvider(encodePassword());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .cors().configurationSource(corsConfigurationSource());

        http.addFilterBefore(formLoginFilter(), UsernamePasswordAuthenticationFilter.class);

        http.authorizeRequests()
                .antMatchers(AUTH_WHITELIST).permitAll();

        http.formLogin().disable();
    }
}
