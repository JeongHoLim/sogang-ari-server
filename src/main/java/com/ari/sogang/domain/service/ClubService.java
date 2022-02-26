package com.ari.sogang.domain.service;

import com.ari.sogang.domain.dto.ClubDto;
import com.ari.sogang.domain.dto.HashTagDto;
import com.ari.sogang.domain.entity.Club;
import com.ari.sogang.domain.entity.ClubHashTag;
import com.ari.sogang.domain.entity.HashTag;
import com.ari.sogang.domain.repository.ClubRepository;
import com.ari.sogang.domain.repository.HashTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClubService{

    private final ClubRepository clubRepository;
    private final HashTagRepository hashTagRepository;

    // 해시태그 리스트를 받으면 해당되는 동아리 리스트 리턴
    public List<ClubDto> searchByHashTag(List<HashTagDto> hashTags){

        var names = hashTags.stream().map(HashTagDto::getName).collect(Collectors.toList());

        return clubRepository.findAllByName(names)
                .stream().map(this::toDto).collect(Collectors.toList());

    }

    // 분과에 해당하는 동아리 리스트 리턴
    public List<ClubDto> searchBySection(String section){
        return clubRepository.findAllBySection(section).stream()
                .map(this::toDto).collect(Collectors.toList());
    }

    // 해당 이름의 동아리 정보 리턴
    public ClubDto searchClubByName(String clubName){
        return toDto(clubRepository.findByName(clubName));
    }

    // 특정 동아리의 해시태그 정보 리턴
    public List<HashTagDto> searchHashTagByName(String clubName){
        var clubHashTags =  clubRepository.findByName(clubName).getClubHashTags();
        var hashTagIds = clubHashTags.stream().map(ClubHashTag::getHashTagId).collect(Collectors.toList());
        return hashTagRepository.findAllById(hashTagIds).stream().map(this::toHashTagDto).collect(Collectors.toList());

    }

    private HashTagDto toHashTagDto(HashTag entity) {
        return HashTagDto.builder()
                .name(entity.getName())
                .build();
    }


    private ClubDto toDto(Club entity) {
        return ClubDto.builder()
                .name(entity.getName())
                .section(entity.getSection())
                .recruiting(entity.isRecruiting())
                .url(entity.getUrl())
                .introduction(entity.getIntroduction())
                .build();
    }


}
