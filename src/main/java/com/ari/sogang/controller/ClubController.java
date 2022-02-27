package com.ari.sogang.controller;

import com.ari.sogang.domain.dto.ClubDto;
import com.ari.sogang.domain.dto.HashTagDto;
import com.ari.sogang.domain.service.ClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/club")
@RequiredArgsConstructor
public class ClubController {
    private final ClubService clubService;

    /* 해시태그 검색 */
    @PostMapping("/search_tag")
    public List<ClubDto> searchByHashTag(@RequestBody List<HashTagDto> hashTagDtos){
        return clubService.searchByHashTag(hashTagDtos);
    }

    /* 분과 검색 */
    @GetMapping("/search_section/{section}")
    public List<ClubDto> searchBySection(@PathVariable String section){
        return clubService.searchBySection(section);
    }

    /* 동아리 정보 */
    @GetMapping("/info/{club_name}")
    public ClubDto getClub(@PathVariable("club_name") String clubName){
        return clubService.searchClubByName(clubName);
    }

    /* 동아리별 해시태그 조회 */
    @GetMapping("/hashtag/{club_name}")
    public List<HashTagDto> getHashTag(@PathVariable("club_name") String clubName){
        return clubService.searchHashTagByName(clubName);
    }
}
