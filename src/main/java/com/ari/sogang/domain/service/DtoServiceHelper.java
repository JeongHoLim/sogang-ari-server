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
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DtoServiceHelper {
    private final PasswordEncoder passwordEncoder;

    private Set<String> getHashTags(Club club){
        return club.getClubHashTags().stream().map(ClubHashTag::getName).collect(Collectors.toSet());
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
                .startDate(club.getStartDate())
                .endDate(club.getEndDate())
                .build();
    }
    public UserDto toDto(User user) {
        return UserDto.builder()
                .name(user.getName())
                .userId(user.getUserId())
                .build();
    }
    public User toEntity(UserDto userDto) {
        return User.builder()
                .userId(userDto.getUserId())
                .name(userDto.getName())
                .enabled(true)
                .password(passwordEncoder.encode(userDto.getPassword()))
                .authorities(new HashSet<UserAuthority>() {
                })
                .build();
    }

}
