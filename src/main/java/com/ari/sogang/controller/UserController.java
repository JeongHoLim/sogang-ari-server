package com.ari.sogang.controller;

import com.ari.sogang.domain.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
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
    @PostMapping("/post-wish/{student_id}/{club_id}")

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
    @GetMapping("/get-wish/{student_id}")
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
    @GetMapping("/get-joined/{student_id}")
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
    @DeleteMapping("/delete-wish/{student_id}/{club_id}")
    @ApiOperation(value = "담아 놓은 동아리 삭제",notes="유저의 담아 놓은 동아리 삭제")
    @PreAuthorize("#studentId == authentication.principal")
    public ResponseEntity<?> updateWishList(@PathVariable("student_id")String studentId, @PathVariable("club_id") Long clubId){
        return userService.updateWishList(studentId,clubId);
    }
}
