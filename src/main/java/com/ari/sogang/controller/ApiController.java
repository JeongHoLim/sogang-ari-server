package com.ari.sogang.controller;

import com.ari.sogang.domain.dto.UserDto;
import com.ari.sogang.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
//@RequestMapping("/api")
public class ApiController {

    private final UserService userService;

    /* 회원가입 */
    @PostMapping("/sign-in")
    public ResponseEntity<UserDto> signUp(@RequestBody UserDto userDto){
        return userService.save(userDto);
    }


    /* 회원 탈퇴 */
    @GetMapping("/sign-out/{student_id}")
    public ResponseEntity<String> signOut(@PathVariable("student_id") String studentId){
        return userService.signOut(studentId);
    }

    /* 중복된 학번 체크 */
    @GetMapping("/check-student-id/{student_id}")
    public boolean checkStudentId(@PathVariable("student_id") String studentId){
        return userService.checkStudentId(studentId);
    }

    /* 이메일로 가입된 계정이 있는지 체크 */
    @GetMapping("/check-email/{student_email}")
    public boolean checkEmail(@PathVariable("student_email")String email){
        return userService.checkEmail(email);
    }
}
