package com.ari.sogang.config.jwt;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import java.util.logging.Logger;

//@Component
//public class JwtInterceptor extends HandlerInterceptor {
////    Logger logger = LoggerFactory.getLogger("io.ojw.mall.interceptor.JwtInterceptor");
////
////    private static final String TOKEN = "jwt-token";
////
////    private JwtService jwtService;
////
////    public JwtInterceptor(JwtService jwtService) {
////        this.jwtService = jwtService;
//    }
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
//            throws Exception {
//        final String token = request.getHeader(TOKEN);
//
//        logger.debug("JwtInterceptor > preHandle > token: " + token);
//
//        if (StringUtils.equals(request.getMethod(), "OPTIONS")) {
//            logger.debug("if request options method is options, return true");
//
//            return true;
//        }
//
//        if(token != null && jwtService.checkToken(token)){
//            return true;
//        }else{
//            throw new UnauthorizedException();
//        }
//    }
//}
