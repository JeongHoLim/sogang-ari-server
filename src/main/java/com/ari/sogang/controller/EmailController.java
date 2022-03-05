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
    public ResponseEntity<?> sendMail(@RequestBody MailFormDto mailFormDto){
        return emailService.sendToken(mailFormDto);
    }

    @PostMapping("/verify-code")
    public ResponseEntity<?> verifyMailCode(@RequestBody MailDto mailDto){

        return emailService.verifyMailCode(mailDto);
    }

    @PostMapping("/send-feedback")
    public ResponseEntity<?> sendFeedback(@RequestBody MailFeedbackDto mailFeedbackDto){
        return emailService.sendFeedback(mailFeedbackDto);
    }


}
