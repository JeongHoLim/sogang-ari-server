package com.ari.sogang.controller;

import com.ari.sogang.config.dto.LoginFormDto;
import com.ari.sogang.config.dto.ResponseDto;
import com.ari.sogang.config.dto.TokenDto;
import com.ari.sogang.domain.dto.PasswordDto;
import com.ari.sogang.domain.dto.UserDto;
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

    /* 회원가입 */
    @ApiResponses(value={
            @ApiResponse(code = 201, message = "회원가입 성공"),
            @ApiResponse(code = 409, message = "중복된 이메일 혹은 중복된 학번")
    })
    @PostMapping(value = "/sign-in")
    @ApiOperation(value = "회원 가입",notes="신규 유저 회원 가입")
    public ResponseEntity<?> signUp(@RequestBody UserDto userDto){
        return userService.save(userDto);
    }

    /* 로그인 */
    @ApiResponses(value={
            @ApiResponse(code = 200, message = "로그인 성공"),
            @ApiResponse(code = 401, message = "로그인 실패")
    })
    @PostMapping("/login")
    @ApiOperation(value = "로그인",notes="유저 로그인")
    public ResponseEntity<?> login(@RequestBody LoginFormDto userLoginFormDto){
        return userService.login(userLoginFormDto);
    }

    /* 로그아웃 */
    @ApiResponses(value={
            @ApiResponse(code = 201, message = "회원가입 성공"),
            @ApiResponse(code = 400, message = "잘못된 요청")
    })
    @PostMapping("/logout")
    @ApiOperation(value = "로그아웃",notes="유저 로그아웃")
    public ResponseEntity<?> logout(@RequestBody TokenDto tokenDto){
        return userService.logout(tokenDto);
    }


    /* 토큰 재발급 */
    @ApiResponses(value={
            @ApiResponse(code = 201, message = "토큰 재발급 성공"),
            @ApiResponse(code = 400, message = "잘못된 요청"),
    })
    @PostMapping("/reissue")
    @ApiOperation(value = "토큰 재발급",notes="엑세스 토큰 만료시, 토큰 재발급")
    public ResponseEntity<?> reissue(@RequestBody TokenDto tokenDto){
        return userService.reissue(tokenDto);
    }

    /* 회원 탈퇴 */
    @ApiResponses(value={
            @ApiResponse(code = 200, message = "회원 탈퇴 성공"),
            @ApiResponse(code = 404, message = "존재하지 않는 유저"),
    })
    @GetMapping("/sign-out/{student_id}")
    @ApiOperation(value = "회원 탈퇴",notes="유저 회원 탈퇴")
    public ResponseEntity<?> signOut(@PathVariable("student_id") String studentId){
        return userService.signOut(studentId);
    }

    /* 중복된 학번 체크 */
    @ApiResponses(value={
            @ApiResponse(code = 200, message = "사용 가능한 학번"),
            @ApiResponse(code = 409, message = "해당 학번으로 가입된 계정이 존재")
    })
    @GetMapping("/check-student-id/{student_id}")
    @ApiOperation(value = "학번 체크",notes="중복된 학번 체크")
    public ResponseEntity<?> checkStudentId(@PathVariable("student_id") String studentId){
        return userService.checkStudentId(studentId);
    }

    /* 이메일로 가입된 계정이 있는지 체크 */
    @ApiResponses(value={
            @ApiResponse(code = 200, message = "사용 가능한 이메일"),
            @ApiResponse(code = 409, message = "해당 이메일로 가입된 계정이 존재")
    })
    @GetMapping("/check-email/{student_email}")
    @ApiOperation(value = "이메일 체크",notes="이메일 중복 체크")
    public ResponseEntity<?> checkEmail(@PathVariable("student_email")String email){
        return userService.checkEmail(email);
    }

    /* 비밀번호 변경 */
    @ApiResponses(value={
            @ApiResponse(code = 200, message = "비밀번호 변경 성공"),
            @ApiResponse(code = 404, message = "존재하지 않는 유저")
    })
    @PostMapping("/change-pwd/{student_id}")
    @ApiOperation(value = "비밀번호 변경",notes="유저 비밀번호 변경")
    public ResponseEntity<?> changePassword(@PathVariable(name = "student_id")String studentId,
                                                 @RequestBody PasswordDto passwordDto){
        return userService.changePassword(studentId,passwordDto);
    }


    /* 비밀번호 분실 */
    @ApiResponses(value={
            @ApiResponse(code = 200, message = "새로운 비밀번호 전송 성공"),
            @ApiResponse(code = 404, message = "존재하지 않는 유저")
    })
    @GetMapping("/reset-pwd/{student_id}")
    @ApiOperation(value = "비밀번호 초기화",notes="유저 비밀번호 유실시, 초기화")
    public ResponseEntity<?> resetPassword(@PathVariable(name = "student_id")String studentId){
        return userService.resetPassword(studentId);
    }
}
