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
        var manager = optionalManager.get();
        var managerId = manager.getId();

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

    public UserAuthority verifyManager(User user){
        for(UserAuthority userAuthority : user.getAuthorities()) {
            if(userAuthority.getAuthority().equals("ROLE_MANAGER")){
                return userAuthority; // success
            }
        }
        return null; // fail
    }

    // 동아리장 위임
//    @Transactional
    public ResponseEntity<?> delegateClub(String managerId, String studentId) {
        var optionalManager =  userRepository.findByStudentId(managerId);
        var optionalUser = userRepository.findByStudentId(studentId);

        if(optionalUser.isEmpty()) return response.fail("등록되지 않은 유저",HttpStatus.NOT_FOUND);
        if(optionalManager.isEmpty()) return response.fail("등록되지 않은 동아리 장",HttpStatus.NOT_FOUND);

        var manager = optionalManager.get();
        var student = optionalUser.get();

        var auth = verifyManager(manager);

        if(auth != null){ // 정상적으로 권한이 있는 경우.
<<<<<<< HEAD
            if(userService.addAuthority(student.getId(), auth.getAuthority(), auth.getClubId())){
//                userRepository.save(student); //새로운 동아리장에게 권한 주고
                if(userService.removeAuthority(manager.getId(), auth.getAuthority(), auth.getClubId()).getStatusCode() == HttpStatus.OK) {
                    manager.getAuthorities().forEach(System.out::println);
//                    userRepository.save(manager);
                    return responseDto.success("동아리장 위임 성공", HttpStatus.OK);
                }
            } else {
=======
            if(userService.addAuthority(student.getId(), auth.getAuthority(), auth.getClubId()) &&
                    userService.removeAuthority(manager.getId(), auth.getAuthority(), auth.getClubId()))
            {
                return responseDto.success("동아리장 위임 성공");
            }
            else {
>>>>>>> 5a3f43c87ca102d7d009a95c7666e057bf594d51
                return responseDto.fail("동아리장 위임 실패", HttpStatus.BAD_REQUEST);
            }
        }
        return responseDto.fail("권한 없음", HttpStatus.FORBIDDEN);
    }

    // 동아리 이름 변경.
    public ResponseEntity<?> updateClubName(String managerId, ClubUpdateDto info) {
        var optionalManager =  userRepository.findByStudentId(managerId);

        if(optionalManager.isEmpty()) return response.fail("등록되지 않은 동아리 장",HttpStatus.NOT_FOUND);

        var manager = optionalManager.get();
        var auth = verifyManager(manager);
        String newName = info.getName();

        if(auth == null){
            return responseDto.fail("권한 없음", HttpStatus.FORBIDDEN);
        } else if(newName == null){
            return responseDto.fail("공백을 입력할 수 없습니다.", HttpStatus.BAD_REQUEST);
        }
        else{
          var club = clubRepository.findById(auth.getClubId()).get();
          club.setName(newName);
          clubRepository.save(club);
          return responseDto.success("동아리 이름 수정 성공.", HttpStatus.OK);
        }
    }

    // 동아리 대문 변경.
    public ResponseEntity<?> updateIntroduction(String managerId, ClubUpdateDto info) {
        var optionalManager =  userRepository.findByStudentId(managerId);

        if(optionalManager.isEmpty()) return response.fail("등록되지 않은 동아리 장",HttpStatus.NOT_FOUND);

        var manager = optionalManager.get();
        var auth = verifyManager(manager);
        String newIntro = info.getIntroduction();

        if(auth == null){
            return responseDto.fail("권한 없음", HttpStatus.FORBIDDEN);
        } else if(newIntro.length() == 0){
            return responseDto.fail("공백을 입력할 수 없습니다.", HttpStatus.BAD_REQUEST);
        }
        else{
            var club = clubRepository.findById(auth.getClubId()).get();
            club.setIntroduction(newIntro);
            clubRepository.save(club);
            return responseDto.success("동아리 대문 수정 성공.", HttpStatus.OK);
        }
    }

    // 동아리 상세 정보 변경.
    public ResponseEntity<?> updateDetail(String managerId, ClubUpdateDto info) {
        var optionalManager =  userRepository.findByStudentId(managerId);

        if(optionalManager.isEmpty()) return response.fail("등록되지 않은 동아리 장",HttpStatus.NOT_FOUND);

        var manager = optionalManager.get();
        var auth = verifyManager(manager);
        String newDetail = info.getDetail();

        if(auth == null){
            return responseDto.fail("권한 없음", HttpStatus.FORBIDDEN);
        } else if(newDetail.length() == 0){
            return responseDto.fail("공백을 입력할 수 없습니다.", HttpStatus.BAD_REQUEST);
        }
        else{
            var club = clubRepository.findById(auth.getClubId()).get();
            club.setDetail(newDetail);
            clubRepository.save(club);
            return responseDto.success("동아리 상세정보 수정 성공.", HttpStatus.OK);
        }
    }

    // 동아리 소개 url 변경.
    public ResponseEntity<?> updateUrl(String managerId, ClubUpdateDto info) {
        var optionalManager =  userRepository.findByStudentId(managerId);

        if(optionalManager.isEmpty()) return response.fail("등록되지 않은 동아리 장",HttpStatus.NOT_FOUND);

        var manager = optionalManager.get();
        var auth = verifyManager(manager);
        String newUrl = info.getUrl();

        if(auth == null){
            return responseDto.fail("권한 없음", HttpStatus.FORBIDDEN);
        } else if(newUrl.length() == 0){
            return responseDto.fail("공백을 입력할 수 없습니다.", HttpStatus.BAD_REQUEST);
        }
        else{
            var club = clubRepository.findById(auth.getClubId()).get();
            club.setUrl(newUrl);
            clubRepository.save(club);
            return responseDto.success("동아리 URL 수정 성공.", HttpStatus.OK);
        }
    }
}
