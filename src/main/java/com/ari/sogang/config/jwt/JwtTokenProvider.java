package com.ari.sogang.config.jwt;

import com.ari.sogang.domain.entity.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.Base64;

@Component
public class JwtTokenProvider {

    private static String JWT_SECRET = "luwak";

    public static final long AUTH_TIME = 60*20;   // 20MIN
    public static final long REFRESH_TIME = 60*60*24*2;  // 2HOUR

    private static Algorithm ALGORITHM;

    @PostConstruct
    private void init(){
        JWT_SECRET = Base64.getEncoder().encodeToString(JWT_SECRET.getBytes());
        ALGORITHM = Algorithm.HMAC512(JWT_SECRET);
    }
    public static String makeAuthToken(User user){

        return JWT.create()
                .withSubject(user.getUsername())
                .withClaim("exp", Instant.now().getEpochSecond()+AUTH_TIME)
                .sign(ALGORITHM)
                ;

    }
    public static String makeRefreshToken(User user){

        return JWT.create()
                        .withSubject(user.getUsername())
                        .withClaim("exp", Instant.now().getEpochSecond()+REFRESH_TIME)
                        .sign(ALGORITHM)
                ;
    }

    public static VerifyResult verfiy(String token){

        try{
            DecodedJWT verified = JWT.require(ALGORITHM).build().verify(token);
            return VerifyResult.builder()
                    .studentId(verified.getSubject())
                    .success(true)
                    .build();
        }
        catch (Exception ex){
            DecodedJWT decoded = JWT.decode(token);
            return VerifyResult.builder().success(false).studentId(decoded.getSubject()).build();
        }
    }

}
