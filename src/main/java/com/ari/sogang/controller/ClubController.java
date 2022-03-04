package com.ari.sogang.controller;

import com.ari.sogang.config.dto.ResponseDto;
import com.ari.sogang.domain.dto.ClubDto;
import com.ari.sogang.domain.dto.HashTagDto;
import com.ari.sogang.domain.service.ClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/club")
@RequiredArgsConstructor
public class ClubController {
    private final ClubService clubService;

    /* 해시태그 검색 */
    @PostMapping("/search_tag")
    public ResponseEntity<?> searchByHashTag(@RequestBody List<HashTagDto> hashTagDtos){
        return clubService.searchByHashTag(hashTagDtos);
    }

    /* 분과 검색 */
    @GetMapping("/search_section/{section}")
    public ResponseEntity<?>  searchBySection(@PathVariable String section){
        return clubService.searchBySection(section);
    }

    /* 동아리 정보 */
    @GetMapping("/info/{club_id}")
    public ResponseEntity<?>  getClub(@PathVariable("club_id") Long clubId){
        return clubService.getClubById(clubId);
    }

    /* 동아리별 해시태그 조회 */
    @GetMapping("/hashtag/{club_id}")
    public ResponseEntity<?>  getHashTag(@PathVariable("club_id") Long clubId){
        return clubService.getHashTagByClubId(clubId);
    }
    /* 동아리 전체 조회 */
    @GetMapping("/find-all")
    public ResponseEntity<?> findAll(){
        return clubService.findAll();
    }
}
