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

    /* 회원가입 */
    @PostMapping("/sign")
    public ResponseEntity signUp(@RequestBody UserDto userDto){
        return userService.save(userDto);
    }

    /* 위시리스트 */
    @GetMapping("/wish/{student_id}")
    public List<UserWishListDto> getWishList(@PathVariable("student_id") String id){
        return userService.getWishList(id);
    }

    /* 가입한 동아리 */
    @GetMapping("/joined/{student_id}")
    public List<ClubDto> searchJoinedClub(@PathVariable("studen_id") String id) {
        return userService.findClub(id);
    }

    /* 동아리 정보 */
    @GetMapping("/club/{club_name}")
    public List<ClubDto> getClub(@PathVariable("club_name") String clubName){
        return userService.getClub(clubName);
    }

    @GetMapping("/hashtag/{club_name}")
    public List<HashTagDto> getHashTag(@PathVariable("club_name") String clubName){
        return userService.getHashTag(clubName);
    }

}