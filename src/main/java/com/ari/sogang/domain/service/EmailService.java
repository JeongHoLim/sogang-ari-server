package com.ari.sogang.domain.service;

import com.ari.sogang.config.dto.ResponseDto;
import com.ari.sogang.domain.dto.MailAlarmDto;
import com.ari.sogang.domain.dto.MailDto;
import com.ari.sogang.domain.dto.MailFeedbackDto;
import com.ari.sogang.domain.dto.MailFormDto;
import com.ari.sogang.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;


import java.util.Objects;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final RedisTemplate<String, String> redisTemplate;
    private final ResponseDto response;

    @Value("${spring.mail.username}")
    private String sogangAriEmail;

    // 유저가 입력한 코드가 보낸 코드와 일치하는지 검증
    // 발급한 시간이랑 지금 시간이랑 비교 && 발급해준 학번이랑 비교
    @Transactional
    protected boolean verify(MailDto mailDto) {
        var flag = redisTemplate.opsForValue().get(mailDto.getToken());
        return !ObjectUtils.isEmpty(flag);
    }

    // 검증 코드 생성해서 해당 이메일로 보냄
    @Async
    @Transactional
    public ResponseEntity<?> sendToken(MailFormDto mailFormDto) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(mailFormDto.getUserId());
        message.setSubject("서강아리 본인 확인 메일");

        var codeToken = createCode();
        message.setText("인증 번호 : " + codeToken);

        // 인증 메일 발송
        javaMailSender.send(message);

        // token 저장 (5분)
        redisTemplate.opsForValue()
                .set(codeToken, "이메일 인증 토큰", 60*5 , TimeUnit.SECONDS);
        // 인증 메일이 오지 않는다면? 사용자한테 이메일 다시 확인하라고 알려줘야 함
        return response.success("인증 코드 전송 성공");
    }

    private String createCode() {
        return Integer.toString( ThreadLocalRandom.current().nextInt(100000, 1000000) );
    }

    @Transactional
    public ResponseEntity<?> sendPassword(String userId,String newPassword) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(userId);
        message.setSubject("서강아리 비밀번호 변경");
        message.setText("변경된 비밀번호 : " + newPassword);

        // 인증 메일 발송
        javaMailSender.send(message);

        return response.success("비밀번호 전송 성공");
    }

    @Transactional
    public ResponseEntity<?> sendFeedback(MailFeedbackDto mailFeedbackDto, MultipartFile file){
        var message = javaMailSender.createMimeMessage();
        try{
            var flg = file!=null;
            var helper = new MimeMessageHelper(message,flg,"utf-8");
            if(flg){
                helper.addAttachment(Objects.requireNonNull(file.getOriginalFilename()),file);
            }
            helper.setSubject(mailFeedbackDto.getTitle());
            helper.setText("From : " + mailFeedbackDto.getEmail()+"\n메시지 : "+mailFeedbackDto.getContent());
            helper.setTo(sogangAriEmail+"@gmail.com");

            javaMailSender.send(message);

        }catch(Exception ex){
            return response.fail("전송 실패",HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return response.success("피드백 전송 성공");
    }


    public ResponseEntity<?> verifyMailCode(MailDto mailDto) {

        if(verify(mailDto))
            return response.success("인증 코드 검사 성공");
        else return response.fail("코드 불일치 혹은 인증 코드 만료", HttpStatus.BAD_REQUEST);

    }

    public void sendAlarm(MailAlarmDto mailAlarmDto){

        var message = new SimpleMailMessage();
        message.setTo(mailAlarmDto.getAddress());
        var content = String.format("담아놓으신 동아리 [%s]가 신규 인원 모집 중으로 변경되었습니다.",mailAlarmDto.getClubName());
        message.setSubject("서강아리 동아리 알림");

        message.setText(content);
        // 인증 메일 발송
        javaMailSender.send(message);

    }
}
