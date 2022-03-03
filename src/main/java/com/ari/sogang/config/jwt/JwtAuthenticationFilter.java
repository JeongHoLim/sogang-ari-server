package com.ari.sogang.config.jwt;

import com.ari.sogang.domain.entity.User;
import com.ari.sogang.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class JwtAuthenticationFilter extends GenericFilterBean {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_TYPE = "Bearer";

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    //    private final RedisTemplate redisTemplate;


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        String token = parseToken((HttpServletRequest) request);

        if(token != null){
            var result = JwtTokenProvider.verfiy(token);
            if(result.isSuccess()){
                var user = (User) userService.loadUserByUsername(result.getStudentId());

                var usernamePasswordToken = new UsernamePasswordAuthenticationToken(
                    user.getStudentId(),null,user.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordToken);
            }
        }

        chain.doFilter(request,response);
    }

    private String parseToken(HttpServletRequest request){
        String bearer = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(bearer == null || !bearer.startsWith("Bearer ")){
            return null;
        }
        return bearer.substring("Bearer ".length());
    }
}
