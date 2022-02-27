package com.ari.sogang.controller;

import com.ari.sogang.domain.dto.ClubDto;
import com.ari.sogang.domain.dto.UserDto;
import com.ari.sogang.domain.dto.UserWishListDto;
import com.ari.sogang.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /* 회원가입 */
    @PostMapping("/sign-in")
    public ResponseEntity signUp(@RequestBody UserDto userDto){

        return userService.save(userDto);
    }

    /* 회원가입 탈퇴 */
    @GetMapping("/sing-out/{student_id}")
    public void signOut(@PathVariable("student_id") String studentId){

    }
    /* 관리자 사이트 */
    @GetMapping("/admin/delete-all")
    public void deleteAll(){

    }

    /* 위시리스트 */
    @GetMapping("/wish/{student_id}")
    public List<UserWishListDto> getWishList(@PathVariable("student_id") String studentId){
        return userService.getWishList(studentId);
    }

    /* 가입한 동아리 */
    @GetMapping("/joined/{student_id}")
    public List<ClubDto> searchJoinedClub(@PathVariable("student_id") String studentId) {
        return userService.findClub(studentId);
    }

    /* 동아리장 사이트 */
    // 동아리 신청 인원 관리

    /* 비밀번호 변경 */

    /* 비밀번호 찾기 */
}
