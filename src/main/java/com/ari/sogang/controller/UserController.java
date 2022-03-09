package com.ari.sogang.controller;

import com.ari.sogang.domain.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /* 담아놓기 저장*/
    @ApiResponses(value={
            @ApiResponse(code = 201, message = "담아 놓기 성공"),
            @ApiResponse(code = 401, message = "로그인 필요"),
            @ApiResponse(code = 403, message = "권한 없음"),
            @ApiResponse(code = 404, message = "존재하지 않는 유저이거나, 존재하지 않는 동아리"),
    })
    @PostMapping("/{student_id}/wish/{club_id}")
    @ApiOperation(value = "담아놓기 ",notes="유저의 담아놓기에 동아리 추가")
    @PreAuthorize("#studentId == authentication.principal")
    public ResponseEntity<?> postWishList(@PathVariable("student_id") String studentId, @PathVariable("club_id") Long clubId){
        return userService.postWishList(studentId,clubId);
    }

    /* 담아놓기 조회*/
    @ApiResponses(value={
            @ApiResponse(code = 200, message = "담아 놓기 성공"),
            @ApiResponse(code = 401, message = "로그인 필요"),
            @ApiResponse(code = 403, message = "권한 없음"),
            @ApiResponse(code = 404, message = "존재하지 않는 유저"),
    })
    @GetMapping("/{student_id}/wish")
    @ApiOperation(value = "담아놓기 조회",notes="유저의 담아 놓은 동아리 조회")
    @PreAuthorize("#studentId == authentication.principal")
    public ResponseEntity<?> getWishList(@PathVariable("student_id") String studentId){
        return userService.getWishList(studentId);
    }

    /* 가입한 동아리 조회 */
    @ApiResponses(value={
            @ApiResponse(code = 200, message = "가입된 동아리 조회 성공"),
            @ApiResponse(code = 401, message = "로그인 필요"),
            @ApiResponse(code = 403, message = "권한 없음"),
            @ApiResponse(code = 404, message = "존재하지 않는 유저"),
    })
    @GetMapping("/{student_id}/joined")
    @ApiOperation(value = "가입된 동아리 조회",notes="유저의 가입된 동아리 조회")
    @PreAuthorize("#studentId == authentication.principal")
    public ResponseEntity<?> getJoinedClub(@PathVariable("student_id") String studentId) {
        return userService.getJoinedClub(studentId);
    }

    /* 담아 놓기 삭제 ~ delete*/
    @ApiResponses(value={
            @ApiResponse(code = 200, message = "담아 놓기 삭제 성공"),
            @ApiResponse(code = 401, message = "로그인 필요"),
            @ApiResponse(code = 403, message = "권한 없음"),
            @ApiResponse(code = 404, message = "존재하지 않는 유저이거나, 존재하지 않는 동아리"),
    })
    @DeleteMapping("/{student_id}/wish/{club_id}")
    @ApiOperation(value = "담아 놓은 동아리 삭제",notes="유저의 담아 놓은 동아리 삭제")
    @PreAuthorize("#studentId == authentication.principal")
    public ResponseEntity<?> updateWishList(@PathVariable("student_id")String studentId, @PathVariable("club_id") Long clubId){
        return userService.updateWishList(studentId,clubId);
    }

    //issue 1
    // 유저가 회원 탈퇴하면 사이트에서만 가입한 동아리가 삭제되는거라
    // 사실상 담아놓기 기능처럼 뺄 수 있는거라 의미 없을거 같은데 (실제로 동아리 탈퇴되는게 아님)
    // 1. 가입신청 처럼 탈퇴 신청처럼 만든다.
    // 2. 가입신청과 같은 맥락에서, 신청을 받으면 저장해 놓을 곳이 필요. => 어떤식으로 저장해두고 매니저에게 보여줄지?
    // 2-1. 프론트 단에서, 신청하면 유저가 신청하면, 리스트로 관리.. etc

//    @DeleteMapping ("/{club_id}/{manager_id}/join/{student_id}")
//    @ApiOperation(value = "동아리 탈퇴",notes="특정 유저 동아리 탈퇴 승인")
//    public ResponseEntity<?> updateJoinedClub(@PathVariable(name = "student_id") String studentId,
//                                              @PathVariable(name = "club_id") Long clubId) {
//        return userService.updateJoinedClub(clubId,studentId);
//    }
}
