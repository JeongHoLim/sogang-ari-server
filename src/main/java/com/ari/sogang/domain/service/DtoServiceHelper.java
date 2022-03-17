package com.ari.sogang.domain.service;

import com.ari.sogang.domain.dto.ClubDto;
import com.ari.sogang.domain.dto.UserDto;
import com.ari.sogang.domain.entity.Club;
import com.ari.sogang.domain.entity.ClubHashTag;
import com.ari.sogang.domain.entity.User;
import com.ari.sogang.domain.entity.UserAuthority;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DtoServiceHelper {
    private final PasswordEncoder passwordEncoder;

    private List<String> getHashTags(Club club){
        return club.getClubHashTags().stream().map(ClubHashTag::getName).collect(Collectors.toList());
    }
    public ClubDto toDto(Club club) {
        return ClubDto.builder()
                .id(club.getId())
                .name(club.getName())
                .section(club.getSection())
                .recruiting(club.isRecruiting())
                .detail(club.getDetail())
                .url(club.getUrl())
                .introduction(club.getIntroduction())
                .location(club.getLocation())
                .hashTags(getHashTags(club))
                .build();
    }
    public UserDto toDto(User user) {
        return UserDto.builder()
                .email(user.getEmail())
                .alarmEmail(user.getAlarmEmail())
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
                .alarmEmail(userDto.getAlarmEmail())
                .authorities(new HashSet<UserAuthority>() {
                })
                .build();
    }
    public Club toEntity(ClubDto clubDto) {
        return Club.builder()
                .id(clubDto.getId())
                .name(clubDto.getName())
                .introduction(clubDto.getIntroduction())
                .detail(clubDto.getDetail())
                .url(clubDto.getUrl())
                .section(clubDto.getSection())
                .recruiting(clubDto.isRecruiting())
                .build();
    }
}
