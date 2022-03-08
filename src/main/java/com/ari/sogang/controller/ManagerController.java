package com.ari.sogang.controller;

import com.ari.sogang.domain.dto.ClubRequestDto;
import com.ari.sogang.domain.dto.ClubUpdateDto;
import com.ari.sogang.domain.service.ManagerService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

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
    @PostMapping("/{club_id}/{manager_id}/join/{student_id}")
    @ApiOperation(value = "동아리 가입 승인",notes="특정 유저 동아리 가입 승인")
    public ResponseEntity<?> postJoinedClub(@PathVariable(name = "student_id") String studentId,
                                            @PathVariable(name = "manager_id") String managerId,
                                            @PathVariable(name = "club_id") Long clubId) {
        return managerService.postJoinedClub(clubId,managerId,studentId);
    }

    /* 동아리장 위임 */
    @ApiResponses(value={
            @ApiResponse(code = 200, message = "동아리 위임 성공"),
            @ApiResponse(code = 400, message = "잘못된 요청"),
            @ApiResponse(code = 403, message = "위임에 대한 권한 없음")
    })
    @GetMapping("{club_id}/{manager_id}/delegate/{student_id}")
    @ApiOperation(value = "동아리장 위임",notes="새로운 동아리 장으로 위임")
    public ResponseEntity<?> delegateClub(@PathVariable(name = "manager_id") String managerId,
                                          @PathVariable(name = "student_id") String studentId,
                                          @PathVariable(name = "club_id") Long clubId){
        return managerService.delegateClub(clubId,managerId,studentId);
    }



    /*모집중 껐다 켰다*/
    @ApiResponses(value={
            @ApiResponse(code = 200, message = "동아리 내용 변경 성공"),
            @ApiResponse(code = 400, message = "잘못된 요청"),
            @ApiResponse(code = 403, message = "권한 없음")
    })
    @PutMapping("/{club_id}/{manager_id}/update")
    @ApiOperation(value = "동아리 모집 변경",notes="동아리 모집 여부 변경")
    public ResponseEntity<?> updateClub(@PathVariable(name = "manager_id") String managerId,
                                              @PathVariable(name = "club_id") Long clubId,
                                              @RequestBody ClubUpdateDto clubUpdateDto){
        return managerService.updateClub(clubId,managerId,clubUpdateDto);
    }


}
