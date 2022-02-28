package com.ari.sogang.domain.service;

import com.ari.sogang.domain.dto.MailDto;
import com.ari.sogang.domain.dto.MailFormDto;
import com.ari.sogang.domain.entity.ConfirmToken;
import com.ari.sogang.domain.entity.User;
import com.ari.sogang.domain.repository.ConfirmTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final ConfirmTokenRepository confirmTokenRepository;

    // 유저가 입력한 코드가 보낸 코드와 일치하는지 검증
    // 발급한 시간이랑 지금 시간이랑 비교 && 발급해준 학번이랑 비교
    @Transactional
    public boolean verify(MailDto mailDto) {

        var optionalToken = confirmTokenRepository.findByToken(mailDto.getToken());
        if(optionalToken.isEmpty()) return false;

        var foundToken = optionalToken.get();

        var ret = foundToken.getStudentId().equals(mailDto.getStudentId())
                && LocalDateTime.now().isBefore(foundToken.getCreatedAt().plusMinutes(5));

        confirmTokenRepository.deleteById(foundToken.getId());

        return ret;
    }

    // 검증 코드 생성해서 해당 이메일로 보냄
    @Async
    @Transactional
    public void sendConfirmToken(MailFormDto mailFormDto) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(mailFormDto.getAddress());
        message.setSubject("서강아리 본인 확인 메일");

        var code = createCode();
        message.setText("인증 번호 : " + code);

        // 인증 메일 발송
        javaMailSender.send(message);

        var token = ConfirmToken.builder()
                .createdAt(LocalDateTime.now())
                .studentId(mailFormDto.getStudentId())
                .token(code)
                .build();

        // token 저장
        confirmTokenRepository.save(token);
    }

    private String createCode() {
        Random random = new Random(); //난수 생성
        StringBuilder key= new StringBuilder();

        for(int i=0;i<3;i++){
            int index = random.nextInt(25)+65;
            key.append((char) index);
        }

        key.append(random.nextInt(1000));

        return key.toString();
    }

    @Transactional
    public void sendPassword(User user,String newPassword) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(user.getEmail());
        message.setSubject("서강아리 비밀번호 변경");
        message.setText("변경된 비밀번호 : " + newPassword);

        // 인증 메일 발송
        javaMailSender.send(message);

    }
}
