package com.ari.sogang.controller;

import com.ari.sogang.domain.dto.UserDto;
import com.ari.sogang.domain.dto.UserWishListDto;
import com.ari.sogang.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiController {

    private final UserService userService;

    @GetMapping("")
    public UserDto home(){
        return userService.
    }

    @PostMapping("")
    public void signUp(@RequestBody UserDto userDto){
//        userService.
        return wishListService.add(wishListDto);
    }

    @GetMapping("/all/{student_id}")
    public List<UserWishListDto> wishList(@PathVariable("student_id") String id){
        return userService.findWishList(id);
    }



}