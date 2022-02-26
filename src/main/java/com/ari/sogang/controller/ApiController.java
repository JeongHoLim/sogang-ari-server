package com.ari.sogang.controller;

import com.ari.sogang.domain.dto.ClubDto;
import com.ari.sogang.domain.dto.HashTagDto;
import com.ari.sogang.domain.dto.UserDto;
import com.ari.sogang.domain.dto.UserWishListDto;
import com.ari.sogang.domain.entity.HashTag;
import com.ari.sogang.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiController {

    private final UserService userService;

    /* 해시태그 검색 */
    @PostMapping("/search_tag")
    public List<ClubDto> searchByHashTag(@RequestBody List<HashTag> hashTags){

    }

    /* 분과 검색 */
    @GetMapping("/section/{section}")
    public List<ClubDto> searchBySection(@PathVariable String section){
        return userService.findClubBySection(section);
    }

    /* 위시리스트 */
    @GetMapping("/user/wish/{student_id}")
    public List<UserWishListDto> getWishList(@PathVariable("student_id") String studentId){
        return userService.getWishList(studentId);
    }

    /* 가입한 동아리 */
    @GetMapping("/user/joined/{student_id}")
    public List<ClubDto> searchJoinedClub(@PathVariable("student_id") String studentId) {
        return userService.findClub(studentId);
    }

    /* 동아리 정보 */
    @GetMapping("/club/{club_name}")
    public List<ClubDto> getClub(@PathVariable("club_name") String clubName){
        return userService.getClub(clubName);
    }

    /* 동아리별 해시태그 조회 */
    @GetMapping("/hashtag/{club_name}")
    public List<HashTagDto> getHashTag(@PathVariable("club_name") String clubName){
        return userService.getHashTag(clubName);
    }

    /* 관리자 사이트 */

    /* 동아리장 사이트 */

    /* 회원가입 */
    @PostMapping("/sign")
    public ResponseEntity signUp(@RequestBody UserDto userDto){
        return userService.save(userDto);
    }

    /* 회원가입 탈퇴 */

    /* 비밀번호 변경 */

    /* 비밀번호 찾기 */
    
}