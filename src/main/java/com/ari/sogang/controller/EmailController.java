package com.ari.sogang.controller;

import com.ari.sogang.domain.dto.MailDto;
import com.ari.sogang.domain.dto.MailFeedbackDto;
import com.ari.sogang.domain.dto.MailFormDto;
import com.ari.sogang.domain.service.EmailService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @ApiResponses(value={
            @ApiResponse(code = 200, message = "인증 코드 전송 성공"),
    })
    @PostMapping("/send-code")
    @ApiOperation(value = "인증 코드 발송",notes="회원 가입 시, 인증 코드 전송")
    public ResponseEntity<?> sendMail(@RequestBody MailFormDto mailFormDto){
        return emailService.sendToken(mailFormDto);
    }

    @ApiResponses(value={
            @ApiResponse(code = 200, message = "코드 인증 성공"),
            @ApiResponse(code = 400, message = "잘못된 인증 코드이거나, 기간 만료"),
    })
    @PostMapping("/verify-code")
    @ApiOperation(value = "인증 코드 검사",notes="발송한 인증 코드 검사")
    public ResponseEntity<?> verifyMailCode(@RequestBody MailDto mailDto){

        return emailService.verifyMailCode(mailDto);
    }

    @ApiResponses(value={
            @ApiResponse(code = 200, message = "피드백 전송 성공")
    })
    @PostMapping("/send-feedback")
    @ApiOperation(value = "피드백/문의 전송",notes="피드백/문의 전송")
    public ResponseEntity<?> sendFeedback(@RequestBody MailFeedbackDto mailFeedbackDto){
        return emailService.sendFeedback(mailFeedbackDto);
    }


}
