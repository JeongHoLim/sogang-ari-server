package com.ari.sogang.controller;

import com.ari.sogang.domain.dto.MailDto;
import com.ari.sogang.domain.dto.MailFeedbackDto;
import com.ari.sogang.domain.dto.MailFormDto;
import com.ari.sogang.domain.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/send-code")
    public void sendMail(@RequestBody MailFormDto mailFormDto){
        emailService.sendConfirmToken(mailFormDto);
    }

    @PostMapping("/verfiy-code")
    public ResponseEntity<String> verfiyMailCode(@RequestBody MailDto mailDto){
        int status;
        String message;
        if(emailService.verify(mailDto)){
            status = 200;
            message = "회원 인증이 완료되었습니다.";
        }
        else{
            status = 401;
            message = "인증 코드가 올바르지 않습니다.";
        }
        return ResponseEntity.status(status).body(message);
    }

    @PostMapping("/send-feedback")
    public void sendFeedback(@RequestBody MailFeedbackDto mailFeedbackDto){
        emailService.sendFeedback(mailFeedbackDto);
    }


}
