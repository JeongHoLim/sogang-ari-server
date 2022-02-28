package com.ari.sogang.controller;

import com.ari.sogang.domain.dto.MailDto;
import com.ari.sogang.domain.dto.MailFormDto;
import com.ari.sogang.domain.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/send-mail")
    public void sendMail(@RequestBody MailFormDto mailFormDto){
        emailService.send(mailFormDto);
    }

    @PostMapping("/verfiy-code")
    public void verfiyMailCode(@RequestBody MailDto mailDto){
        emailService.verify(mailDto);
    }
}
