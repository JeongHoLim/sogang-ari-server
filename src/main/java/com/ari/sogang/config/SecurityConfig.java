package com.ari.sogang.config;

import com.ari.sogang.config.jwt.*;
import com.ari.sogang.domain.service.DtoServiceHelper;
import com.ari.sogang.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

    private final UserService userService;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final RedisTemplate redisTemplate;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        JwtAuthenticationFilter loginFilter = new JwtAuthenticationFilter(userService,redisTemplate);
        http
                .csrf().disable()
                .cors()/*.configurationSource(corsConfigurationSource())*/
                .and()
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(loginFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
<<<<<<< HEAD
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .antMatchers("/api/user/**").hasAuthority("USER")
                .antMatchers("/api/user_manager/**").hasAuthority("USER_MANAGER")
                .antMatchers("/api/admin/**").hasAuthority("ADMIN")
//                .anyRequest().denyAll()
=======
                .antMatchers("/api/user/**").hasRole("USER")
                .antMatchers("/api/manager/**").hasRole("USER_MANAGER")
                .antMatchers("/api/admin/**").hasRole("ADMIN")
<<<<<<< HEAD
>>>>>>> 7c32609442862874756c3c4bb3e4f05c60271eba
=======
                .anyRequest().permitAll()
>>>>>>> c74e56baffa20b7d0bdc3f6ee6f1be9b3127bd30
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)
                ;
    }

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**").allowedOrigins("*");
//        WebMvcConfigurer.super.addCorsMappings(registry);
//    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        var configuration = new CorsConfiguration();

        configuration.addAllowedOrigin("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);

        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;

    }
}
