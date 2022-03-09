package com.ari.sogang.domain.service;

import com.ari.sogang.config.dto.ResponseDto;
import com.ari.sogang.domain.dto.ClubRequestDto;
import com.ari.sogang.domain.dto.ClubUpdateDto;
import com.ari.sogang.domain.entity.Club;
import com.ari.sogang.domain.entity.User;
import com.ari.sogang.domain.entity.UserAuthority;
import com.ari.sogang.domain.entity.UserClub;
import com.ari.sogang.domain.repository.ClubRepository;
import com.ari.sogang.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;


@Service
@RequiredArgsConstructor
public class ManagerService {
    private final UserRepository userRepository;
    private final ClubRepository clubRepository;
    private final ResponseDto response;
    private final UserService userService;
    private final ResponseDto responseDto;

    @Transactional
    public ResponseEntity<?> postJoinedClub(Long clubId,String managerId, String studentId){

        var optionalUser = userRepository.findByStudentId(studentId);
        var optionalClub = clubRepository.findById(clubId);
        var optionalManager = userRepository.findByStudentId(managerId);

        if(optionalUser.isEmpty()) return response.fail("해당 유저가 존재하지 않습니다.", HttpStatus.NOT_FOUND);
        if(optionalClub.isEmpty()) return response.fail("등록되지 않은 동아리입니다.",HttpStatus.NOT_FOUND);
        if(optionalManager.isEmpty()) return response.fail("해당 동아리장은 존재하지 않는 유저입니다.",HttpStatus.NOT_FOUND);

        var user = optionalUser.get();
        var club = optionalClub.get();
        var manager = optionalManager.get();

        Long userId = user.getId();
        if(!isValidManager(manager,clubId)){
            return response.fail("권한 없음",HttpStatus.FORBIDDEN);
        }

        if(user.getUserClubs() == null)
            user.setUserClubs(new ArrayList<>());

        var newClub = new UserClub(userId, club);

        // 동아리 없으면 추가
        if(user.getUserClubs().contains(newClub))
            return response.fail("해당 유저는 이미 가입되어있습니다.",HttpStatus.CONFLICT);
        else
            user.getUserClubs().add(newClub);

        /* 영속성 전이 cacade에 의해 DB 저장 */
        userRepository.save(user);

        return response.success("동아리 가입 성공",HttpStatus.CREATED);
    }



    private boolean isValidManager(User manager,Long clubId){
        return manager.getAuthorities().contains(
                UserAuthority.builder()
                        .clubId(clubId)
                        .userId(manager.getId())
                        .authority("ROLE_MANAGER")
                        .build());
    }

    // 동아리장 위임
    @Transactional
    public ResponseEntity<?> delegateClub(Long clubId,String managerId, String studentId) {
        var optionalManager =  userRepository.findByStudentId(managerId);
        var optionalUser = userRepository.findByStudentId(studentId);

        if (optionalUser.isEmpty()) return response.fail("등록되지 않은 유저", HttpStatus.NOT_FOUND);
        if (optionalManager.isEmpty()) return response.fail("등록되지 않은 동아리 장", HttpStatus.NOT_FOUND);

        var manager = optionalManager.get();
        var student = optionalUser.get();


        // 이 부분 살짝 찜찜
        if(isValidManager(manager,clubId)){ // 정상적으로 권한이 있는 경우.
            if(userService.addAuthority(student.getId(),"ROLE_MANAGER", clubId) &&
                    userService.removeAuthority(manager.getId(), "ROLE_MANAGER",clubId))
            {
                return responseDto.success("동아리장 위임 성공");
            }
            else {
                return responseDto.fail("동아리장 위임 실패", HttpStatus.BAD_REQUEST);
            }
        }
        return responseDto.fail("권한 없음", HttpStatus.FORBIDDEN);
    }

    @Transactional
    public ResponseEntity<?> updateClub(Long clubId,String managerId,ClubUpdateDto clubUpdateInfo) {
        var optionalClub =  clubRepository.findById(clubId);
        var optionalManager =  userRepository.findByStudentId(managerId);

        if(optionalClub.isEmpty()) return response.fail("등록되지 않은 동아리",HttpStatus.NOT_FOUND);
        if(optionalManager.isEmpty()) return response.fail("등록되지 않은 동아리 장",HttpStatus.NOT_FOUND);

        var manager = optionalManager.get();
        var manageId = manager.getId();

        if(!manager.getAuthorities().contains(
                UserAuthority.builder()
                        .clubId(clubId)
                        .userId(manageId)
                        .authority("ROLE_MANAGER")
                        .build())){
            return response.fail("권한 없음",HttpStatus.FORBIDDEN);
        }

        Club club = optionalClub.get();

        var flag = clubUpdateInfo.getRecruit();

        var clubName = clubUpdateInfo.getName();
        var clubDetail = clubUpdateInfo.getDetail();
        var clubUrl = clubUpdateInfo.getUrl();
        var clubIntro = clubUpdateInfo.getIntroduction();

        if("yes".equals(flag))
            club.setRecruiting(true);
        else if("no".equals(flag))
            club.setRecruiting(false);
        else return response.fail("잘못된 요청",HttpStatus.BAD_REQUEST);

        // 예외 처리는 나중에~

        club.setName(clubName);
        club.setDetail(clubDetail);
        club.setUrl(clubUrl);
        club.setIntroduction(clubIntro);

        clubRepository.save(club);

        return response.success("모집 설정 성공");
    }

    // 동아리 이름 변경.
//    private ResponseEntity<?> updateClubName(String managerId, ClubUpdateDto info) {
//        var optionalManager =  userRepository.findByStudentId(managerId);
//
//        if(optionalManager.isEmpty()) return response.fail("등록되지 않은 동아리 장",HttpStatus.NOT_FOUND);
//
//        var manager = optionalManager.get();
//        var auth = verifyManager(manager);
//        String newName = info.getName();
//
//        if(auth == null){
//            return responseDto.fail("권한 없음", HttpStatus.FORBIDDEN);
//        } else if(newName == null){
//            return responseDto.fail("공백을 입력할 수 없습니다.", HttpStatus.BAD_REQUEST);
//        }
//        else{
//          var club = clubRepository.findById(auth.getClubId()).get();
//          club.setName(newName);
//          clubRepository.save(club);
//          return responseDto.success("동아리 이름 수정 성공.", HttpStatus.OK);
//        }
//    }
//
//    // 동아리 대문 변경.
//    public ResponseEntity<?> updateIntroduction(String managerId, ClubUpdateDto info) {
//        var optionalManager =  userRepository.findByStudentId(managerId);
//
//        if(optionalManager.isEmpty()) return response.fail("등록되지 않은 동아리 장",HttpStatus.NOT_FOUND);
//
//        var manager = optionalManager.get();
//        var auth = verifyManager(manager);
//        String newIntro = info.getIntroduction();
//
//        if(auth == null){
//            return responseDto.fail("권한 없음", HttpStatus.FORBIDDEN);
//        } else if(newIntro.length() == 0){
//            return responseDto.fail("공백을 입력할 수 없습니다.", HttpStatus.BAD_REQUEST);
//        }
//        else{
//            var club = clubRepository.findById(auth.getClubId()).get();
//            club.setIntroduction(newIntro);
//            clubRepository.save(club);
//            return responseDto.success("동아리 대문 수정 성공.", HttpStatus.OK);
//        }
//    }
//
//    // 동아리 상세 정보 변경.
//    public ResponseEntity<?> updateDetail(String managerId, ClubUpdateDto info) {
//        var optionalManager =  userRepository.findByStudentId(managerId);
//
//        if(optionalManager.isEmpty()) return response.fail("등록되지 않은 동아리 장",HttpStatus.NOT_FOUND);
//
//        var manager = optionalManager.get();
//        var auth = verifyManager(manager);
//        String newDetail = info.getDetail();
//
//        if(auth == null){
//            return responseDto.fail("권한 없음", HttpStatus.FORBIDDEN);
//        } else if(newDetail.length() == 0){
//            return responseDto.fail("공백을 입력할 수 없습니다.", HttpStatus.BAD_REQUEST);
//        }
//        else{
//            var club = clubRepository.findById(auth.getClubId()).get();
//            club.setDetail(newDetail);
//            clubRepository.save(club);
//            return responseDto.success("동아리 상세정보 수정 성공.", HttpStatus.OK);
//        }
//    }
//
//    // 동아리 소개 url 변경.
//    public ResponseEntity<?> updateUrl(String managerId, ClubUpdateDto info) {
//        var optionalManager =  userRepository.findByStudentId(managerId);
//
//        if(optionalManager.isEmpty()) return response.fail("등록되지 않은 동아리 장",HttpStatus.NOT_FOUND);
//
//        var manager = optionalManager.get();
//        var auth = verifyManager(manager);
//        String newUrl = info.getUrl();
//
//        if(auth == null){
//            return responseDto.fail("권한 없음", HttpStatus.FORBIDDEN);
//        } else if(newUrl.length() == 0){
//            return responseDto.fail("공백을 입력할 수 없습니다.", HttpStatus.BAD_REQUEST);
//        }
//        else{
//            var club = clubRepository.findById(auth.getClubId()).get();
//            club.setUrl(newUrl);
//            clubRepository.save(club);
//            return responseDto.success("동아리 URL 수정 성공.", HttpStatus.OK);
//        }
//    }
}
