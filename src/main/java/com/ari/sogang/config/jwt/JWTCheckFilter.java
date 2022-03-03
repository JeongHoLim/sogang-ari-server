package com.ari.sogang.config.jwt;


import com.ari.sogang.domain.entity.User;
import com.ari.sogang.domain.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTCheckFilter extends BasicAuthenticationFilter {

    private UserService userService;

    public JWTCheckFilter(AuthenticationManager authenticationManager, UserService userService) {
        super(authenticationManager);
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws IOException, ServletException
    {
        String bearer = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(bearer == null || !bearer.startsWith("Bearer ")){
            chain.doFilter(request,response);
            return;
        }
        String token = bearer.substring("Bearer ".length());

        VerifyResult verifyResult = JwtProvider.verfiy(token);

        if(verifyResult.isSuccess()){
            User user = (User) userService.loadUserByUsername(verifyResult.getStudentId());

            var usernamePasswordToken = new UsernamePasswordAuthenticationToken(
                user.getStudentId(),null,user.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordToken);
        }
        if("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        }else {
            chain.doFilter(request, response);
        }
    }
}
