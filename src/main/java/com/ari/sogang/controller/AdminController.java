package com.ari.sogang.controller;

import com.ari.sogang.config.dto.ResponseDto;
import com.ari.sogang.domain.service.AdminService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    /* 관리자 사이트 */
    /* 동아리장 승인 */
    @ApiResponses(value={
            @ApiResponse(code = 201, message = "동아리장 등록 성공"),
            @ApiResponse(code = 404, message = "존재하지 않는 유저이거나, 존재하지 않는 동아리"),
    })
    @GetMapping("/register/{manager_id}/{club_id}")
    @ApiOperation(value = "동아리장 등록",notes="동아리장 등록 ")
    public ResponseEntity<?> registerManager(@PathVariable(name = "manager_id")String managerId,
                                             @PathVariable(name = "club_id")Long clubId){
        return adminService.registerManager(managerId,clubId);
    }
}
