package com.ari.sogang.controller;

import com.ari.sogang.domain.dto.ClubDto;
import com.ari.sogang.domain.service.ManagerService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/manager")
@RequiredArgsConstructor
public class ManagerController {
    private final ManagerService managerService;
    /* 동아리장 사이트 */
    // 동아리 신청 인원 관리
    /* 가입한 동아리 저장 */
    @ApiResponses(value={
            @ApiResponse(code = 201, message = "토큰 재발급 성공"),
            @ApiResponse(code = 404, message = "존재 하지 않는 유저"),
    })
    // 유저가 리스트로 오고, 클럽 아이디가 와야 할 듯
    @PostMapping("/post-joined/{student_id}")
    @ApiOperation(value = "동아리 가입 승인",notes="특정 유저 동아리 가입 승인")
    public ResponseEntity<?> postJoinedClub(@PathVariable("student_id") String studentId, @RequestBody List<ClubDto> clubDtos){
        return managerService.postJoinedClub(studentId,clubDtos);
    }
    /*모집중 껐다 켰다*/

    @ApiResponses(value={
            @ApiResponse(code = 200, message = "모집 설정 성공"),
            @ApiResponse(code = 400, message = "잘못된 요청")
    })
    @GetMapping("/set-recruit/{club_id}")
    @ApiOperation(value = "동아리 모집 설정",notes="동아리 모집 여부 설정")
    public ResponseEntity<?> setRecruiting(@PathVariable("club_id")Long clubId,@RequestParam("recruit") String flag){
        return managerService.setRecruiting(clubId, flag);
    }
}
