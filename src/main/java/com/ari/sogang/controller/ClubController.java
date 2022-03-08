package com.ari.sogang.controller;

import com.ari.sogang.config.dto.ResponseDto;
import com.ari.sogang.domain.dto.ClubDto;
import com.ari.sogang.domain.dto.HashTagDto;
import com.ari.sogang.domain.service.ClubService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
    @ApiResponses(value={
            @ApiResponse(code = 200, message = "동아리 검색 성공")
    })
    @PostMapping("/tag")
    @ApiOperation(value = "해시태그로 검색",notes="해시태그로 동아리 검색")
    public ResponseEntity<?> searchByHashTag(@RequestBody List<HashTagDto> hashTagDtos){
        return clubService.searchByHashTag(hashTagDtos);
    }

    /* 분과 검색 */
    @ApiResponses(value={
            @ApiResponse(code = 200, message = "동아리 검색 성공")
    })
    @GetMapping("/section")
    @ApiOperation(value = "분과로 동아리 검색",notes="분과로 동아리 검색")
    public ResponseEntity<?>  searchBySection(@RequestParam String section){
        return clubService.searchBySection(section);
    }

    @ApiResponses(value={
            @ApiResponse(code = 200, message = "동아리 검색 성공"),
    })
    @ApiOperation(value = "이름으로 동아리 검색",notes="이름으로 동아리 검색")
    @GetMapping("/name/")
    public ResponseEntity<?> searchByName(@RequestParam String clubName){
        return clubService.searchByName(clubName);
    }

    /* 동아리 정보 */
    @ApiResponses(value={
            @ApiResponse(code = 200, message = "동아리 조회 성공")
    })
    @GetMapping("/{club_id}/info")
    @ApiOperation(value = "동아리 조회",notes="동아리 정보 조회")
    public ResponseEntity<?>  getClub(@PathVariable("club_id") Long clubId){
        return clubService.getClubById(clubId);
    }

    /* 동아리별 해시태그 조회 */
    @ApiResponses(value={
            @ApiResponse(code = 200, message = "해시태그 조회 성공")
    })
    @GetMapping("/{club_id}/tag")
    @ApiOperation(value = "해시태그 조회",notes="특정 동아리 해시태그 조회")
    public ResponseEntity<?>  getHashTag(@PathVariable("club_id") Long clubId){
        return clubService.getHashTagByClubId(clubId);
    }
    /* 동아리 전체 조회 */
    @ApiResponses(value={
            @ApiResponse(code = 200, message = "전체 동아리 조회 성공")
    })
    @GetMapping("/all")
    @ApiOperation(value = "동아리 전체 조회",notes="전체 동아리 정보 조회")
    public ResponseEntity<?> findAll(){
        return clubService.findAll();
    }
}
