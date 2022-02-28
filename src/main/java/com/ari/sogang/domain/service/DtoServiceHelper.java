package com.ari.sogang.domain.service;

import com.ari.sogang.domain.dto.ClubDto;
import com.ari.sogang.domain.dto.HashTagDto;
import com.ari.sogang.domain.dto.UserDto;
import com.ari.sogang.domain.entity.Club;
import com.ari.sogang.domain.entity.HashTag;
import com.ari.sogang.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DtoServiceHelper {
    private final PasswordEncoder passwordEncoder;

    public HashTagDto toDto(HashTag entity) {
        return HashTagDto.builder()
                .name(entity.getName())
                .build();
    }

    public ClubDto toDto(Club entity) {
        return ClubDto.builder()
                .name(entity.getName())
                .section(entity.getSection())
                .recruiting(entity.isRecruiting())
                .detail(entity.getDetail())
                .url(entity.getUrl())
                .introduction(entity.getIntroduction())
                .build();
    }
    public UserDto toDto(User user) {
        return UserDto.builder()
                .email(user.getEmail())
                .major(user.getMajor())
                .name(user.getName())
                .studentId(user.getStudentId())
                .build();
    }
    public User toEntity(UserDto userDto) {
        return User.builder()
                .studentId(userDto.getStudentId())
                .name(userDto.getName())
                .major(userDto.getMajor())
                .enabled(true)
                .password(passwordEncoder.encode(userDto.getPassword()))
                .email(userDto.getEmail())
                .build();
    }
    public Club toEntity(ClubDto clubDto) {
        return Club.builder()
                .name(clubDto.getName())
                .introduction(clubDto.getIntroduction())
                .detail(clubDto.getDetail())
                .url(clubDto.getUrl())
                .section(clubDto.getSection())
                .recruiting(clubDto.isRecruiting())
                .build();
    }
    public HashTag toEntity(HashTagDto hashTagDto) {
        return HashTag.builder()
                .name(hashTagDto.getName())
                .build();
    }
}
