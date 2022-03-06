package com.ari.sogang.domain.service;

import com.ari.sogang.config.dto.ResponseDto;
import com.ari.sogang.domain.dto.ClubDto;
import com.ari.sogang.domain.entity.Club;
import com.ari.sogang.domain.entity.User;
import com.ari.sogang.domain.entity.UserClub;
import com.ari.sogang.domain.repository.ClubRepository;
import com.ari.sogang.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ManagerService {
    private final UserRepository userRepository;
    private final ClubRepository clubRepository;
    private final ResponseDto response;


    @Transactional
    public ResponseEntity<?> postJoinedClub(String studentId, List<ClubDto> clubDtos){
        List<UserClub> userClubs = new ArrayList<>();
        var optionalUser = userRepository.findByStudentId(studentId);
        if(optionalUser.isEmpty()) return response.fail("해당 유저가 존재하지 않습니다.", HttpStatus.NOT_FOUND);

        var user = optionalUser.get();

        Long userId = user.getId();
        /* 동아리 장에 의해 가입된 동아리 추가*/
        for(ClubDto temp : clubDtos){
            var clubId = clubRepository.findByName(temp.getName()).getId();
            userClubs.add(new UserClub(clubId,userId));
        }
        /* 영속성 전이 cacade에 의해 DB 저장 */
        user.setUserClubs(userClubs);
        userRepository.save(user);

        return response.success("동아리 가입 성공",HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity<?> setRecruiting(Long clubId, String flag) {
        Club club = clubRepository.findById(clubId).get();

        if(flag.equals("yes"))
            club.setRecruiting(true);
        else if(flag.equals("no"))
            club.setRecruiting(false);
        else return response.fail("잘못된 요청",HttpStatus.BAD_REQUEST);
        clubRepository.save(club);

        return response.success("모집 설정 성공");
    }
}
