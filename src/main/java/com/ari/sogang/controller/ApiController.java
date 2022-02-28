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
    public ResponseEntity<?> signUp(@RequestBody UserDto userDto){
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
        return userService.isValidStudentId(studentId);
    }

    /* 이메일로 가입된 계정이 있는지 체크 */
    @GetMapping("/check-email/{student_email}")
    public boolean checkEmail(@PathVariable("student_email")String email){
        return userService.isValidEmail(email);
    }
    /* 비밀번호 변경 */
    @PostMapping("/change-pwd/{student_id}")
    public ResponseEntity<String> changePassword(@PathVariable(name = "student_id")String studentId,
        @RequestBody String password){
        return userService.changePassword(studentId,password);
    }


    /* 비밀번호 분실 */
    @GetMapping("/lost-pwd/{student_id}")
    public ResponseEntity<String> resetPassword(@PathVariable(name = "student_id")String studentId){
        return userService.resetPassword(studentId);
    }
}
