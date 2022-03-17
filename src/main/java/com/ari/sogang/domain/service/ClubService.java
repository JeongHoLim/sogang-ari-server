package com.ari.sogang.domain.service;

import com.ari.sogang.config.dto.ResponseDto;
import com.ari.sogang.domain.entity.Club;
import com.ari.sogang.domain.entity.ClubHashTag;
import com.ari.sogang.domain.repository.ClubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClubService{
    private final ClubRepository clubRepository;
    private final DtoServiceHelper dtoServiceHelper;
    private final ResponseDto responseDto;

    @Value("${spring.logo.path}")
    private String path;

    @Value("${spring.logo.folder}")
    private String folder;

    // 분과(체육 분과 등 총 6개)에 해당하는 동아리 리스트 리턴
    public ResponseEntity<?> searchBySection(String section){
        var candidates = clubRepository.findAllBySection(section);
        return responseDto.success(
            candidates.stream().map(dtoServiceHelper::toDto).collect(Collectors.toList())
            ,"동아리 조회 성공"
        );
    }

    public ResponseEntity<?> searchByName(String clubName) {
        var candidates = clubRepository.findByNameContains(clubName);
        return responseDto.success(
                candidates.stream().map(dtoServiceHelper::toDto).collect(Collectors.toList())
                ,"동아리 검색 성공"
        );
    }

    // 해당 이름의 동아리 정보 리턴
    public ResponseEntity<?> getClubById(Long clubId){
        var club = clubRepository.findById(clubId).get();
        return responseDto.success( dtoServiceHelper.toDto(club), "동아리 조회 성공");
    }

    public ResponseEntity<?> findAll() {
        var clubList = new ArrayList<>();
        for(Club club : clubRepository.findAll()){
            clubList.add(dtoServiceHelper.toDto(club));
        }
        return responseDto.success(
                clubList,
                "동아리 조회 성공"
        );
    }

    public ResponseEntity<?> getLogo(Long clubId) {

        var resource = new FileSystemResource(path+folder + clubId+".png");

        HttpHeaders header = new HttpHeaders();

        var fileName = clubId + ".png";

        if(!resource.exists()){
            fileName = "0.png";
            resource = new FileSystemResource(path+folder+fileName);
        }

        try {
            var filePath = Paths.get(path + folder + fileName);
            header.add("Content-type", Files.probeContentType(filePath));
        }
        catch (IOException e) {
            e.printStackTrace();
            return responseDto.fail("서버 오류",HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);


    }
}
