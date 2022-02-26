package com.ari.sogang.config;

import com.ari.sogang.domain.entity.User;
import com.ari.sogang.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class DbInit {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    void createUser(){
        User user1,user2;
        if (!userService.find("user1").isPresent()) {
            user1 = userService.save(User.builder()
                    .email("user1")
                    .password(passwordEncoder.encode("1111"))
                    .enabled(true)
                    .build());
            userService.addAuthority(user1.getId(),"ROLE_USER");
//            userService.removeAuthority(user1.getId(),"ROLE_USER");
        }
        if (!userService.find("user2").isPresent()) {
            user2 = userService.save(User.builder()
                    .email("user2")
                    .password(passwordEncoder.encode("1111"))
                    .enabled(true)
                    .build());
            userService.addAuthority(user2.getId(),"ROLE_USER");
//            userService.removeAuthority(user2.getId(),"ROLE_USER");
        }

    }
}
