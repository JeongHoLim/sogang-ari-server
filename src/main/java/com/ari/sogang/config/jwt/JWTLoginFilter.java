package com.ari.sogang.config.jwt;

import com.ari.sogang.config.UserLoginForm;
import com.ari.sogang.domain.entity.User;
import com.ari.sogang.domain.service.DtoServiceHelper;
import com.ari.sogang.domain.service.UserService;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter {

    private ObjectMapper objectMapper = new ObjectMapper();
    private UserService userService;
    private DtoServiceHelper dtoServiceHelper;
    DaoAuthenticationProvider s;
    public JWTLoginFilter(AuthenticationManager authenticationManager, UserService userService,DtoServiceHelper dtoServiceHelper) {
        super(authenticationManager);
        setFilterProcessesUrl("/api/login");
        this.userService = userService;
        this.dtoServiceHelper = dtoServiceHelper;
    }

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request, HttpServletResponse response
    ) throws AuthenticationException
    {
        RequestWrapper wrapper = new RequestWrapper(request);

        byte[] body = StreamUtils.copyToByteArray(wrapper.getInputStream());

        UserLoginForm userLogin = objectMapper.readValue(body, UserLoginForm.class);
        if (userLogin.getRefreshToken() == null) {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    userLogin.getStudentId(), userLogin.getPassword(), null
            );
            return getAuthenticationManager().authenticate(token);

        } else {
            VerifyResult result = JwtProvider.verfiy(userLogin.getRefreshToken());
            if(result.isSuccess()){
                User user = (User) userService.loadUserByUsername(result.getStudentId());
                return new UsernamePasswordAuthenticationToken(
                        user,user.getAuthorities()
                );
            }
            else{
                throw new TokenExpiredException("refreshToken expired");
            }

        }
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain, Authentication authResult) throws IOException, ServletException
    {

        User user = (User) authResult.getPrincipal();
        response.setHeader("auth_token", JwtProvider.makeAuthToken(user));
        response.setHeader("refresh_token", JwtProvider.makeRefreshToken(user));

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().write(objectMapper.writeValueAsBytes(dtoServiceHelper.toDto(user)));

    }
}
