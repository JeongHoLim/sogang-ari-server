package com.ari.sogang.domain.service;

import com.ari.sogang.config.dto.ResponseDto;
import com.ari.sogang.domain.dto.ClubDto;
import com.ari.sogang.domain.dto.HashTagDto;
import com.ari.sogang.domain.entity.Club;
import com.ari.sogang.domain.entity.ClubHashTag;
import com.ari.sogang.domain.entity.HashTag;
import com.ari.sogang.domain.repository.ClubRepository;
import com.ari.sogang.domain.repository.HashTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    private final DtoServiceHelper dtoServiceHelper;
    private final ResponseDto response;


    // 해시태그 리스트를 받으면 해당되는 동아리 리스트 리턴
    public ResponseEntity<?> searchByHashTag(List<HashTagDto> hashTags){
        /* HashTagDto의 name을 사용하여 HashTag Entity들을 hashList에 저장. */
        var nameList = hashTags.stream().map(HashTagDto::getName).collect(Collectors.toList());
        var hashList = nameList.stream()
                .map(hashTagRepository::findByName).collect(Collectors.toList());

        List<ClubDto> clubList = new ArrayList<>();

        for(Club club : clubRepository.findAll()){
            var hashTagIds = club.getClubHashTags().stream()
                    .map(ClubHashTag::getHashTagId)
                    .collect(Collectors.toList());
            var hashTagList = hashTagIds.stream()
                    .map(hashTagRepository::findById).collect(Collectors.toList());
            /* hashList와 비교해서, 해당 HashTag Entity들을 모두 포함하고 있는 club이면 추가.*/
            if(hashTagList.containsAll(hashList)){
                /* Dto로 변환 */
                clubList.add(dtoServiceHelper.toDto(club));
            }
        }
        return response.success(clubList,"동아리 조회 성공");
    }

    // 분과(체육 분과 등 총 6개)에 해당하는 동아리 리스트 리턴
    public ResponseEntity<?> searchBySection(String section){
        return response.success(
            clubRepository.findAllBySection(section).stream()
                .map(dtoServiceHelper::toDto).collect(Collectors.toList())
            ,"동아리 조회 성공"
        );
    }


    public ResponseEntity<?> searchByName(String clubName) {

        var candidates = clubRepository.findByNameContains(clubName);
        return response.success(
                candidates,"동아리 검색 성공"
        );

    }



    // 해당 이름의 동아리 정보 리턴
    public ResponseEntity<?> getClubById(Long clubId){
        return response.success(
                dtoServiceHelper.toDto(clubRepository.findById(clubId).get()),
                        "동아리 조회 성공"
        );
    }

    // 특정 동아리의 해시태그 정보 리턴
    public ResponseEntity<?> getHashTagByClubId(Long clubId){
        var clubHashTags =  clubRepository.findById(clubId).get().getClubHashTags();
        var hashTagIds = clubHashTags.stream().map(ClubHashTag::getHashTagId).collect(Collectors.toList());

        return response.success(
            hashTagRepository.findAllById(hashTagIds).stream().map(dtoServiceHelper::toDto).collect(Collectors.toList())
                ,"해시 태그 조회 성공"
        );
    }


    public ResponseEntity<?> findAll() {
        var clubList = new ArrayList<>();
        for(Club club : clubRepository.findAll()){
            clubList.add(dtoServiceHelper.toDto(club));
        }
        return response.success(
                clubList,
                "동아리 조회 성공"
        );
    }

}
