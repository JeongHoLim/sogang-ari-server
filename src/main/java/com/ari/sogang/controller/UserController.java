package com.ari.sogang.controller;

import com.ari.sogang.domain.dto.UserDto;
import com.ari.sogang.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /* 회원가입 */
    @PostMapping("/sign")
    public ResponseEntity signUp(@RequestBody UserDto userDto){
        return userService.save(userDto);
    }

    /* 회원가입 탈퇴 */
    @GetMapping("/secede/{student_id}")
    public void signOut(@PathVariable("student_id") String studentId){

    }
    /* 관리자 사이트 */
    @GetMapping("/delete_all")
    public void deleteAll(){

    }

    /* 동아리장 사이트 */
    // 동아리 신청 인원 관리

    /* 비밀번호 변경 */

    /* 비밀번호 찾기 */
}
