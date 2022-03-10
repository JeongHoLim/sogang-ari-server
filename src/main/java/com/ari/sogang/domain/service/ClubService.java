package com.ari.sogang.domain.service;

import com.ari.sogang.config.dto.ResponseDto;
import com.ari.sogang.domain.entity.Club;
import com.ari.sogang.domain.entity.ClubHashTag;
import com.ari.sogang.domain.repository.ClubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClubService{
    private final ClubRepository clubRepository;
    private final DtoServiceHelper dtoServiceHelper;
    private final ResponseDto response;


    private List<String> getHashTags(Club club){
        return club.getClubHashTags().stream().map(ClubHashTag::getName).collect(Collectors.toList());
    }
    // 분과(체육 분과 등 총 6개)에 해당하는 동아리 리스트 리턴
    public ResponseEntity<?> searchBySection(String section){

        return response.success(
            clubRepository.findAllBySection(section).stream()
                .map(
                        club -> {
                            var dto = dtoServiceHelper.toDto(club);
                            dto.setHashTags(getHashTags(club));
                            return dto;
                        }
                ).collect(Collectors.toList())
            ,"동아리 조회 성공"
        );
    }


    public ResponseEntity<?> searchByName(String clubName) {
        var candidates = clubRepository.findByNameContains(clubName);
        return response.success(
                candidates.stream().map(
                        club -> {
                            var dto = dtoServiceHelper.toDto(club);
                            dto.setHashTags(getHashTags(club));
                            return dto;
                        }
                ).collect(Collectors.toList()),"동아리 검색 성공"
        );

    }



    // 해당 이름의 동아리 정보 리턴
    public ResponseEntity<?> getClubById(Long clubId){
        var club = clubRepository.findById(clubId).get();
        var dto =dtoServiceHelper.toDto(club);
        dto.setHashTags(getHashTags(club));
        return response.success( dto, "동아리 조회 성공");
    }

    // 특정 동아리의 해시태그 정보 리턴
//    이 기능도 따로 존재할 필요 없을 듯? --------------------------------------------
//    public ResponseEntity<?> getHashTagByClubId(Long clubId){
////        var clubHashTags =  clubRepository.findById(clubId).get().getClubHashTags();
//        var optionalClub = clubRepository.findById(clubId);
//        if(optionalClub.isEmpty())
//            return response.fail("CLUB_NOT_EXIST", HttpStatus.NOT_FOUND);
//        var club = optionalClub.get();
//        var hashTagIds = clubHashTags.stream().map(ClubHashTag::getHashTagId).collect(Collectors.toList());
//
//        return response.success(
//            hashTagRepository.findAllById(hashTagIds).stream().map(dtoServiceHelper::toDto).collect(Collectors.toList())
//                ,"해시 태그 조회 성공"
//        );
//    }


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
