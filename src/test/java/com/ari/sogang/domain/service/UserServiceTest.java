package com.ari.sogang.domain.service;

import com.ari.sogang.domain.dto.UserDto;
import com.ari.sogang.domain.entity.User;
import com.ari.sogang.domain.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest extends WebIntegrationTest{

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    @Test
    @DisplayName("1. 유저 생성, 권한 부여, 삭제, 조회")
    void test_1(){
        var user = User.builder()
                        .email("ijh9404@sogang.ac.kr")
                        .studentId("20171682")
                        .enabled(true)
                        .name("임정호")
                        .major("컴퓨터공학과")
                        .password("1111")
                .build();

        userRepository.save(user);

        userService.addAuthority(user.getId(),"ROLE_USER");
        System.out.println(userRepository.findByStudentId("20171682").get());

        userService.removeAuthority(user.getId(),"ROLE_USER");

        System.out.println(userRepository.findByStudentId("20171682").get());

    }

    @Test
    @DisplayName("2. 회원 가입, 탈퇴")
    void test_2(){

        TestRestTemplate client = new TestRestTemplate();

        var newUser = UserDto.builder()
                .studentId("20171682")
                .name("임정호")
                .major("컴퓨터공학과")
                .email("ijh9404@sogang.ac.kr")
                .password("1111")
                .build();

        var entity = new HttpEntity<UserDto>(newUser);
        ResponseEntity<String> response1 = client.exchange(
                uri("/sign-in"), HttpMethod.POST,entity,String.class);

        System.out.println(response1.getBody());

        ResponseEntity<String> response2 = client.getForEntity(uri("/sign-out","20171682"),String.class);
        System.out.println(response2.getBody());

    }
}