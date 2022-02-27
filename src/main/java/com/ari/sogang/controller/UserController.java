package com.ari.sogang.controller;

import com.ari.sogang.domain.dto.ClubDto;
import com.ari.sogang.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /* 위시리스트 저장*/
    @PostMapping("/post-wish/{student_id}")
    public void postWishList(@PathVariable("student_id") String studentId, @RequestBody List<ClubDto> clubDtos){
        userService.postWishList(studentId,clubDtos);
    }

    /* 위시리스트 조회*/
    @GetMapping("/get-wish/{student_id}")
    public List<ClubDto> getWishList(@PathVariable("student_id") String studentId){
        return userService.getWishList(studentId);
    }

    /* 가입한 동아리 저장 */
    @PostMapping("/post-joined/{club_name}")
    public void postJoinedClub(@PathVariable("club_name") String clubName,@RequestBody List<ClubDto> clubDtos){
        userService.postJoinedClub(clubName,clubDtos);
    }

    /* 가입한 동아리 조회 */
    @GetMapping("/get-joined/{student_id}")
    public List<ClubDto> getJoinedClub(@PathVariable("student_id") String studentId) {
        return userService.getJoinedClub(studentId);
    }

    /* 비밀번호 변경 */

    /* 비밀번호 찾기 */
}
