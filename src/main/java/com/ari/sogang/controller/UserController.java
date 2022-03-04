package com.ari.sogang.controller;

import com.ari.sogang.config.dto.ResponseDto;
import com.ari.sogang.domain.dto.ClubDto;
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

    /* 위시리스트 저장*/
    @PostMapping("/post-wish/{student_id}/{club_id}")
    public ResponseEntity<?> postWishList(@PathVariable("student_id") String studentId, @PathVariable("club_id") Long clubId){
        return userService.postWishList(studentId,clubId);
    }

    /* 위시리스트 조회*/
    @GetMapping("/get-wish/{student_id}")
    public ResponseEntity<?> getWishList(@PathVariable("student_id") String studentId){
        return userService.getWishList(studentId);
    }

    /* 가입한 동아리 조회 */
    @GetMapping("/get-joined/{student_id}")
    public ResponseEntity<?> getJoinedClub(@PathVariable("student_id") String studentId) {
        return userService.getJoinedClub(studentId);
    }

    /* 위시리스트 수정 ~ delete*/
    @DeleteMapping("/delete-wish/{student_id}/{club_id}")
    public ResponseEntity<?> updateWishList(@PathVariable("student_id")String studentId, @PathVariable("club_id") Long clubId){
        return userService.updateWishList(studentId,clubId);
    }
}
