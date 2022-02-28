package com.ari.sogang.domain.service;

import com.ari.sogang.domain.dto.ClubDto;
import com.ari.sogang.domain.entity.Club;
import com.ari.sogang.domain.entity.User;
import com.ari.sogang.domain.entity.UserClub;
import com.ari.sogang.domain.repository.ClubRepository;
import com.ari.sogang.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ManagerService {
    private final UserRepository userRepository;
    private final ClubRepository clubRepository;
    private final DtoServiceHelper dtoServiceHelper;

    @Transactional
    public void postJoinedClub(String studentId, List<ClubDto> clubDtos){
        List<UserClub> userClubs = new ArrayList<>();
        User user = userRepository.findByStudentId(studentId).get();
        Long userId = user.getId();
        /* 동아리 장에 의해 가입된 동아리 추가*/
        for(ClubDto temp : clubDtos){
            var clubId = clubRepository.findByName(temp.getName()).getId();
            userClubs.add(new UserClub(clubId,userId));
        }
        /* 영속성 전이 cacade에 의해 DB 저장 */
        user.setUserClubs(userClubs);
        userRepository.save(user);
    }

    @Transactional
    public void setRecruiting(String clubName, String flag) {
        Club club = clubRepository.findByName(clubName);
        if(flag.equals("yes"))
            club.setRecruiting(true);
        else if(flag.equals("no"))
            club.setRecruiting(false);
        clubRepository.save(club);
    }
}
