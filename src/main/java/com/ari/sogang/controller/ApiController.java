package com.ari.sogang.controller;

import com.ari.sogang.config.dto.LoginRequestDto;
import com.ari.sogang.config.dto.TokenDto;
import com.ari.sogang.domain.dto.PasswordDto;
import com.ari.sogang.domain.dto.UserDto;
import com.ari.sogang.domain.service.ApiService;
import com.ari.sogang.domain.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ApiController {

    private final UserService userService;
    private final ApiService apiService;

    /* 회원가입 */
    @ApiResponses(value={
            @ApiResponse(code = 201, message = "회원가입 성공"),
            @ApiResponse(code = 409, message = "중복된 이메일 혹은 중복된 학번")
    })
    @PostMapping(value = "/sign-up")
    @ApiOperation(value = "회원 가입",notes="신규 유저 회원 가입")
    public ResponseEntity<?> signUp(@RequestBody UserDto userDto){
        return apiService.save(userDto);
    }

    /* 로그인 */
    @ApiResponses(value={
            @ApiResponse(code = 200, message = "로그인 성공"),
            @ApiResponse(code = 401, message = "로그인 실패")
    })
    @PostMapping("/login")
    @ApiOperation(value = "로그인",notes="유저 로그인")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto){
        return apiService.login(loginRequestDto);
    }

    /* 로그아웃 */
    @ApiResponses(value={
            @ApiResponse(code = 200, message = "로그아웃 성공"),
            @ApiResponse(code = 400, message = "잘못된 요청")
    })
    @PostMapping("/logout")
    @ApiOperation(value = "로그아웃",notes="유저 로그아웃")
    public ResponseEntity<?> logout(@RequestBody TokenDto tokenDto){
        return apiService.logout(tokenDto);
    }


    /* 토큰 재발급 */
    @ApiResponses(value={
            @ApiResponse(code = 201, message = "토큰 재발급 성공"),
            @ApiResponse(code = 400, message = "잘못된 요청"),
    })
    @PostMapping("/reissue")
    @ApiOperation(value = "토큰 재발급",notes="엑세스 토큰 만료시, 토큰 재발급")
    public ResponseEntity<?> reissue(@RequestBody TokenDto tokenDto){
        return apiService.reissue(tokenDto);
    }

    /* 중복된 학번 체크 */
    @ApiResponses(value={
            @ApiResponse(code = 200, message = "사용 가능한 이메일"),
            @ApiResponse(code = 409, message = "해당 이메일로 가입된 계정이 존재")
    })
    @GetMapping("/check-id/{user_id}")
    @ApiOperation(value = "이메일 검사",notes="중복된 이메일 검사")
    public ResponseEntity<?> checkUserId(@PathVariable("user_id") String userId){
        return apiService.checkUserId(userId);
    }


    @GetMapping("/addAdmin")
    public void addAdmin(){
        userService.addAdmin();
    }
}
