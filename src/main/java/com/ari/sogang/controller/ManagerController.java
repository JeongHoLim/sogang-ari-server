package com.ari.sogang.controller;

import com.ari.sogang.domain.dto.ClubDto;
import com.ari.sogang.domain.dto.ClubRequestDto;
import com.ari.sogang.domain.service.ManagerService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
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
            @ApiResponse(code = 201, message = "동아리 가입 승인 성공"),
            @ApiResponse(code = 403, message = "권한 없음"),
            @ApiResponse(code = 404, message = "존재 하지 않는 유저, 존재하지 않는 동아리, 존재하지 않는 동아리장"),
    })
    @PostMapping("/post-joined/{student_id}")
    @ApiOperation(value = "동아리 가입 승인",notes="특정 유저 동아리 가입 승인")
    public ResponseEntity<?> postJoinedClub(@PathVariable(name = "student_id") String studentId,
                                            @RequestBody ClubRequestDto clubRequestDto) throws AccessDeniedException {
        return managerService.postJoinedClub(clubRequestDto,studentId);
    }
    /*모집중 껐다 켰다*/

    @ApiResponses(value={
            @ApiResponse(code = 200, message = "동아리 모집 설정 성공"),
            @ApiResponse(code = 400, message = "잘못된 요청"),
            @ApiResponse(code = 403, message = "권한 없음")
    })
    @PutMapping("/set-recruit")
    @ApiOperation(value = "동아리 모집 설정",notes="동아리 모집 여부 설정")
    public ResponseEntity<?> setRecruiting(@RequestBody ClubRequestDto clubRequestDto,
                                           @RequestParam("recruit") String flag){
        return managerService.setRecruiting(clubRequestDto, flag);
    }
}
