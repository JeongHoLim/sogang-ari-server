package com.ari.sogang.controller;

import com.ari.sogang.domain.dto.PasswordDto;
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
    @PostMapping("/{user_id}/wish/{club_id}")
    @ApiOperation(value = "담아놓기",notes="유저의 담아놓기에 동아리 추가")
    @PreAuthorize("#userId == authentication.principal")
    public ResponseEntity<?> postWishList(@PathVariable("user_id") String userId, @PathVariable("club_id") Long clubId){
        return userService.postWishList(userId,clubId);
    }

    @ApiResponses(value={
            @ApiResponse(code = 201, message = "동아리 가입 신청 성공"),
            @ApiResponse(code = 401, message = "로그인 필요"),
            @ApiResponse(code = 403, message = "권한 없음"),
            @ApiResponse(code = 404, message = "존재하지 않는 유저이거나, 존재하지 않는 동아리"),
    })
    @PostMapping("/{user_id}/join/{club_id}")
    @ApiOperation(value = "가입 신청 ",notes="동아리 가입 신청")
    @PreAuthorize("#userId == authentication.principal")
    public ResponseEntity<?> joinClub(@PathVariable("user_id") String userId, @PathVariable("club_id") Long clubId){
        return userService.joinClub(userId,clubId);
    }

    /* 담아놓기 조회*/
    @ApiResponses(value={
            @ApiResponse(code = 200, message = "담아 놓기 조회 성공"),
            @ApiResponse(code = 401, message = "로그인 필요"),
            @ApiResponse(code = 403, message = "권한 없음"),
            @ApiResponse(code = 404, message = "존재하지 않는 유저"),
    })
    @GetMapping("/{user_id}/wish")
    @ApiOperation(value = "담아놓기 조회",notes="유저의 담아 놓은 동아리 조회")
    @PreAuthorize("#userId == authentication.principal")
    public ResponseEntity<?> getWishList(@PathVariable("user_id") String userId){
        return userService.getWishList(userId);
    }

    /* 가입한 동아리 조회 */
    @ApiResponses(value={
            @ApiResponse(code = 200, message = "가입된 동아리 조회 성공"),
            @ApiResponse(code = 401, message = "로그인 필요"),
            @ApiResponse(code = 403, message = "권한 없음"),
            @ApiResponse(code = 404, message = "존재하지 않는 유저"),
    })
    @GetMapping("/{user_id}/join")
    @ApiOperation(value = "가입된 동아리 조회",notes="유저의 가입된 동아리 조회")
    @PreAuthorize("#userId == authentication.principal")
    public ResponseEntity<?> getJoinedClub(@PathVariable("user_id") String userId) {
        return userService.getJoinedClub(userId);
    }

    /* 담아 놓기 삭제 ~ delete*/
    @ApiResponses(value={
            @ApiResponse(code = 200, message = "담아 놓기 삭제 성공"),
            @ApiResponse(code = 401, message = "로그인 필요"),
            @ApiResponse(code = 403, message = "권한 없음"),
            @ApiResponse(code = 404, message = "존재하지 않는 유저이거나, 존재하지 않는 동아리"),
    })
    @DeleteMapping("/{user_id}/wish/{club_id}")
    @ApiOperation(value = "담아 놓은 동아리 삭제",notes="유저의 담아 놓은 동아리 삭제")
    @PreAuthorize("#userId == authentication.principal")
    public ResponseEntity<?> updateWishList(@PathVariable("user_id")String userId, @PathVariable("club_id") Long clubId){
        return userService.updateWishList(userId,clubId);
    }


    /* 비밀번호 변경 */
    @ApiResponses(value={
            @ApiResponse(code = 200, message = "비밀번호 변경 성공"),
            @ApiResponse(code = 404, message = "존재하지 않는 유저")
    })
    @PutMapping("/{user_id}/pwd/change")
    @ApiOperation(value = "비밀번호 변경",notes="유저 비밀번호 변경")
    @PreAuthorize("#userId == authentication.principal")
    public ResponseEntity<?> changePassword(@PathVariable(name = "user_id")String userId,
                                            @RequestBody PasswordDto passwordDto){
        return userService.changePassword(userId,passwordDto);
    }


    /* 비밀번호 분실 */
    @ApiResponses(value={
            @ApiResponse(code = 200, message = "새로운 비밀번호 전송 성공"),
            @ApiResponse(code = 404, message = "존재하지 않는 유저")
    })
    @PutMapping("/{user_id}/pwd/reset")
    @PreAuthorize("#userId == authentication.principal")
    @ApiOperation(value = "비밀번호 초기화",notes="유저 비밀번호 유실시, 초기화")
    public ResponseEntity<?> resetPassword(@PathVariable(name = "user_id")String userId){
        return userService.resetPassword(userId);
    }


    /* 회원 탈퇴 */
    @ApiResponses(value={
            @ApiResponse(code = 200, message = "회원 탈퇴 성공"),
            @ApiResponse(code = 404, message = "존재하지 않는 유저"),
    })
    @DeleteMapping("/{user_id}")
    @ApiOperation(value = "회원 탈퇴",notes="유저 회원 탈퇴")
    @PreAuthorize("#userId == authentication.principal")
    public ResponseEntity<?> signOut(@PathVariable("user_id") String userId){
        return userService.signOut(userId);
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
