package com.ari.sogang.controller;

import com.ari.sogang.domain.dto.ClubDto;
import com.ari.sogang.domain.service.ManagerService;
import lombok.RequiredArgsConstructor;
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
    @PostMapping("/post-joined/{student_id}")
    public void postJoinedClub(@PathVariable("student_id") String studentId, @RequestBody List<ClubDto> clubDtos){
        managerService.postJoinedClub(studentId,clubDtos);
    }

    /*모집중 껐다 켰다*/
}
