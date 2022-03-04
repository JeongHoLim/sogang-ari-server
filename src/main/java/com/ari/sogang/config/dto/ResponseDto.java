package com.ari.sogang.config.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;

@Component
public class ResponseDto {

    @Builder
    @Getter
    @Setter
    private static class Body{

        private int status;
        private String result;
        private String message;
        private Object data;
        private Object error;
    }

//    Response 객체
//    {
//        "state" : 200,
//        "result" : success,
//        "message" : message,
//        "data" : data,
//        "error" : error
//    }

    public ResponseEntity<?> success(Object data, String message, HttpStatus status){

        Body body = Body.builder()
                .status(status.value())
                .data(data)
                .message(message)
                .result("success")
                .error(Collections.emptyList())
                .build();
        return ResponseEntity.status(status).body(body);
    }
    // message만 있는 성공 응답
    public ResponseEntity<?> success(String message){
        return success(Collections.emptyList(),message,HttpStatus.OK);
    }

    public ResponseEntity<?>success(Object data,String message){
        return success(data,message,HttpStatus.OK);
    }
    public ResponseEntity<?>success(String message,HttpStatus status){
        return success(message,status);
    }


    // 올바른 요청이지만 처리에는 실패
    public ResponseEntity<?> fail(Object data,String message,HttpStatus status){

        Body body = Body.builder()
                    .status(status.value())
                    .data(data)
                    .message(message)
                    .result("fail")
                    .error(Collections.emptyList())
                    .build();
        return ResponseEntity.status(status).body(body);
    }

    // 실패 응답
    public ResponseEntity<?> fail(String msg, HttpStatus status) {
        return fail(Collections.emptyList(), msg, status);
    }


    // 올바르지 않은 요청
    public ResponseEntity<?> invalidFields(LinkedList<LinkedHashMap<String, String>> errors) {
        Body body = Body.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .data(Collections.emptyList())
                .result("fail")
                .message("")
                .error(errors)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

}
