package com.ari.sogang.domain.service;

import com.ari.sogang.config.dto.ResponseDto;
import com.ari.sogang.domain.dto.ClubRequestDto;
import com.ari.sogang.domain.entity.Club;
import com.ari.sogang.domain.entity.UserAuthority;
import com.ari.sogang.domain.entity.UserClub;
import com.ari.sogang.domain.repository.ClubRepository;
import com.ari.sogang.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class ManagerService {
    private final UserRepository userRepository;
    private final ClubRepository clubRepository;
    private final ResponseDto response;


    @Transactional
    public ResponseEntity<?> postJoinedClub(ClubRequestDto clubRequestDto,String studentId){

        var clubId = clubRequestDto.getClubId();
        var managerStudentId = clubRequestDto.getManagerId();

        var optionalUser = userRepository.findByStudentId(studentId);
        var optionalClub = clubRepository.findById(clubId);
        var optionalManager = userRepository.findByStudentId(managerStudentId);

        if(optionalUser.isEmpty()) return response.fail("해당 유저가 존재하지 않습니다.", HttpStatus.NOT_FOUND);
        if(optionalClub.isEmpty()) return response.fail("등록되지 않은 동아리입니다.",HttpStatus.NOT_FOUND);
        if(optionalManager.isEmpty()) return response.fail("해당 동아리장은 존재하지 않는 유저입니다.",HttpStatus.NOT_FOUND);

        var user = optionalUser.get();
        var managerId = optionalManager.get().getId();

        Long userId = user.getId();
        if(!user.getAuthorities().contains(
                UserAuthority.builder()
                        .clubId(clubId)
                        .userId(managerId)
                        .authority("ROLE_MANAGER")
                .build())){
            return response.fail("권한 없음",HttpStatus.FORBIDDEN);
        }


        if(user.getUserClubs() == null)
            user.setUserClubs(new ArrayList<>());

        var userClubs = user.getUserClubs();
        var newClub = new UserClub(clubId,userId);

        // 동아리 없으면 추가
        if(userClubs.contains(newClub))
            return response.fail("해당 유저는 이미 가입되어있습니다.",HttpStatus.CONFLICT);

        userClubs.add(newClub);

        /* 영속성 전이 cacade에 의해 DB 저장 */

        user.setUserClubs(userClubs);
        userRepository.save(user);

        return response.success("동아리 가입 성공",HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity<?> updateRecruiting(ClubRequestDto clubRequestDto, String flag) {
        var optionalClub =  clubRepository.findById(clubRequestDto.getClubId());
        var optionalManager =  userRepository.findByStudentId(clubRequestDto.getManagerId());

        if(optionalClub.isEmpty()) return response.fail("등록되지 않은 동아리",HttpStatus.NOT_FOUND);
        if(optionalManager.isEmpty()) return response.fail("등록되지 않은 동아리 장",HttpStatus.NOT_FOUND);

        var manager = optionalManager.get();
        var managerId = manager.getId();

        if(!manager.getAuthorities().contains(
                UserAuthority.builder()
                        .clubId(clubRequestDto.getClubId())
                        .userId(managerId)
                        .authority("ROLE_MANAGER")
                        .build())){
            return response.fail("권한 없음",HttpStatus.FORBIDDEN);
        }

        Club club = optionalClub.get();

        if(flag.equals("yes"))
            club.setRecruiting(true);
        else if(flag.equals("no"))
            club.setRecruiting(false);
        else return response.fail("잘못된 요청",HttpStatus.BAD_REQUEST);

        clubRepository.save(club);

        return response.success("모집 설정 성공");
    }
}
