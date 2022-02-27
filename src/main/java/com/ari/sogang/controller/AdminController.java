package com.ari.sogang.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    /* 관리자 사이트 */
    @GetMapping("/admin/delete-all")
    public void deleteAll(){

    }
}
