package com.ari.sogang.config;

import com.ari.sogang.config.jwt.JWTCheckFilter;
import com.ari.sogang.config.jwt.JWTLoginFilter;
import com.ari.sogang.config.jwt.JwtAccessDeniedHandler;
import com.ari.sogang.config.jwt.JwtAuthenticationEntryPoint;
import com.ari.sogang.domain.service.DtoServiceHelper;
import com.ari.sogang.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private final DtoServiceHelper dtoServiceHelper;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        JWTLoginFilter loginFilter = new JWTLoginFilter(authenticationManager(),userService,dtoServiceHelper);
        JWTCheckFilter checkFilter = new JWTCheckFilter(authenticationManager(),userService);

        http
                .antMatcher("/api/**")
                .csrf().disable()
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(checkFilter, BasicAuthenticationFilter.class)

                .authorizeRequests()
                .antMatchers("/api/user/**").hasAnyRole("ROLE_USER")
                .antMatchers("/api/user_manager/**").hasAnyRole("ROLE_USER_MANAGER")
                .antMatchers("/api/admin/**").hasAnyRole("ROLE_ADMIN")
//                .anyRequest().denyAll()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                ;
    }
}
