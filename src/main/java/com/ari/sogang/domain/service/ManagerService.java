package com.ari.sogang.domain.service;

import com.ari.sogang.config.dto.ResponseDto;
import com.ari.sogang.domain.dto.ClubUpdateDto;
import com.ari.sogang.domain.dto.MailAlarmDto;
import com.ari.sogang.domain.entity.*;
import com.ari.sogang.domain.repository.ClubRepository;
import com.ari.sogang.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ManagerService {
    private final UserRepository userRepository;
    private final ClubRepository clubRepository;
    private final UserService userService;
    private final ResponseDto responseDto;

    private final EmailService emailService;
    private final RedisTemplate<String, String> redisTemplate;

    @Transactional
    public ResponseEntity<?> postJoinedClub(Long clubId,String managerId, String studentId){

        var optionalUser = userRepository.findByUserId(studentId);
        var optionalClub = clubRepository.findById(clubId);
        var optionalManager = userRepository.findByUserId(managerId);

        if(optionalUser.isEmpty()) return responseDto.fail("해당 유저가 존재하지 않습니다.", HttpStatus.NOT_FOUND);
        if(optionalClub.isEmpty()) return responseDto.fail("등록되지 않은 동아리입니다.",HttpStatus.NOT_FOUND);
        if(optionalManager.isEmpty()) return responseDto.fail("해당 동아리장은 존재하지 않는 유저입니다.",HttpStatus.NOT_FOUND);

        var user = optionalUser.get();
        var club = optionalClub.get();
        var manager = optionalManager.get();

        Long userId = user.getId();
        if(!isValidManager(manager,clubId)){
            return responseDto.fail("권한 없음",HttpStatus.FORBIDDEN);
        }


        if(user.getUserClubs() == null)
            user.setUserClubs(new ArrayList<>());

        var userClubs = user.getUserClubs();
        var newClub = new UserClub(clubId,userId);

        // 동아리 없으면 추가
        if(userClubs.contains(newClub)){
            club.getClubUsers().remove(new ClubUser(clubId,userId));
            clubRepository.save(club);
            return responseDto.fail("해당 유저는 이미 가입되어있습니다.",HttpStatus.CONFLICT);
        }

        userClubs.add(newClub);
        club.getClubUsers().remove(new ClubUser(clubId,userId));
        /* 영속성 전이 cacade에 의해 DB 저장 */

        user.setUserClubs(userClubs);

        userRepository.save(user);
        clubRepository.save(club);

        return responseDto.success("동아리 가입 성공",HttpStatus.CREATED);
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
        var optionalManager =  userRepository.findByUserId(managerId);

        var optionalUser = userRepository.findByUserId(studentId);

        if (optionalUser.isEmpty()) return responseDto.fail("등록되지 않은 유저", HttpStatus.NOT_FOUND);
        if (optionalManager.isEmpty()) return responseDto.fail("등록되지 않은 동아리 장", HttpStatus.NOT_FOUND);

        var manager = optionalManager.get();
        var student = optionalUser.get();

        if(isValidManager(manager,clubId)){ // 정상적으로 권한이 있는 경우.
            if(userService.addAuthority(student.getId(),"ROLE_MANAGER", clubId))
            {
                userService.signOut(managerId); //기존 동아리장 회원 탈퇴
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
        var optionalManager =  userRepository.findByUserId(managerId);

        if(optionalClub.isEmpty()) return responseDto.fail("등록되지 않은 동아리",HttpStatus.NOT_FOUND);
        if(optionalManager.isEmpty()) return responseDto.fail("등록되지 않은 동아리 장",HttpStatus.NOT_FOUND);

        var manager = optionalManager.get();

        if(!isValidManager(manager,clubId)){
            return responseDto.fail("권한 없음",HttpStatus.FORBIDDEN);
        }

        Club club = optionalClub.get();

        var flag = clubUpdateInfo.getRecruit();

        var clubName = clubUpdateInfo.getName();
        var clubDetail = clubUpdateInfo.getDetail();
        var clubUrl = clubUpdateInfo.getUrl();
        var clubIntro = clubUpdateInfo.getIntroduction();
        var clubHashTags = clubUpdateInfo.getHashTags();
        var clubRecruitStart = clubUpdateInfo.getStartDate();
        var clubRecruitEnd = clubUpdateInfo.getEndDate();

        if("yes".equals(flag))
            club.setRecruiting(true);
        else if("no".equals(flag))
            club.setRecruiting(false);
        else return responseDto.fail("잘못된 요청",HttpStatus.BAD_REQUEST);

        // 예외 처리는 나중에~

        club.setName(clubName);
        club.setDetail(clubDetail);
        club.setUrl(clubUrl);
        club.setIntroduction(clubIntro);
        club.setStartDate(clubRecruitStart);
        club.setEndDate(clubRecruitEnd);

        var deleted = club.getClubHashTags();
        club.getClubHashTags().removeAll(deleted);

        var res = clubHashTags.stream().map(ht -> new ClubHashTag(ht,clubId)).collect(Collectors.toSet());
        for(var r : res)
            club.getClubHashTags().add(r);

        clubRepository.save(club);

        return responseDto.success("모집 설정 성공");
    }

    public ResponseEntity<?> getCandidates(Long clubId, String managerId) {


        var optionalUser = userRepository.findByUserId(managerId);
        var optionalClub = clubRepository.findById(clubId);

        if(optionalClub.isEmpty()) return responseDto.fail("CLUB_NOT_EXIST",HttpStatus.NOT_FOUND);
        if(optionalUser.isEmpty()) return responseDto.fail("USER_NOT_EXIST",HttpStatus.NOT_FOUND);

        var manager = optionalUser.get();
        var club = optionalClub.get();

        if(!isValidManager(manager,clubId))
            return responseDto.fail("권한 없음",HttpStatus.FORBIDDEN);

        var candidates = club.getClubUsers();

        return responseDto.success(candidates,"지원자 조회 성공");
    }

    public ResponseEntity<?> alarm(Long clubId, String managerId) {

        var optionalClub = clubRepository.findById(clubId);
        if(optionalClub.isEmpty()) return responseDto.fail("CLUB_NOT_EXIST",HttpStatus.NOT_FOUND);

        var optionalManager= userRepository.findByUserId(managerId);
        if(optionalManager.isEmpty()) return responseDto.fail("CLUB_NOT_EXIST",HttpStatus.NOT_FOUND);

        var manager = optionalManager.get();

        if(!isValidManager(manager,clubId)){
            return responseDto.fail("권한 없음",HttpStatus.FORBIDDEN);
        }
        var club = optionalClub.get();


        if(redisTemplate.opsForValue().get(club.getName())!=null){
            return responseDto.fail("알림 기능을 이용한지 아직 하루가 지나지 않았습니다.",HttpStatus.BAD_REQUEST);
        }

        var userList =  club.getUserWishClubs().stream().map(
                u -> userRepository.findById(u.getUserId()).get()
        ).collect(Collectors.toList());


        for(var user : userList){
            var mailForm = MailAlarmDto.builder()
                    .address(user.getUserId()).clubName(club.getName()).build();
            emailService.sendAlarm(mailForm);
        }

        // test로 2분만 해놨음
        redisTemplate.opsForValue()
                .set(club.getName(), "알림 기능", 2 , TimeUnit.MINUTES);


        return responseDto.success("알림 메일 전송 성공");
    }
}
