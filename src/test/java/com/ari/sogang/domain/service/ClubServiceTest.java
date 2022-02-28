package com.ari.sogang.domain.service;

import com.ari.sogang.domain.dto.ClubDto;
import com.ari.sogang.domain.entity.Club;
import com.ari.sogang.domain.entity.User;
import com.ari.sogang.domain.repository.ClubRepository;
import com.ari.sogang.domain.repository.HashTagRepository;
import com.ari.sogang.domain.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ClubServiceTest {
    @Autowired
    ClubService clubService;
    @Autowired
    ClubRepository clubRepository;

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

    @Autowired
    ManagerService managerService;
    @Autowired
    HashTagRepository hashTagRepository;

    @Autowired
    DtoServiceHelper dtoServiceHelper;

    @Test
    @DisplayName("1. ManagerController : 가입한 동아리 추가(postJoinedClub)")
    void postJoinedTest(){
        System.out.println(">>>>>>>>>>>> Test Start.");

        User user = userRepository.findByStudentId("20171500").get();
        List<ClubDto> wishClubs = new ArrayList<>();
        for(Club club : clubRepository.findAll()){
            if(club.getId() > 3)
                break;
            var comp = dtoServiceHelper.toDto(club);
            wishClubs.add(comp);
        }

        managerService.postJoinedClub(user.getStudentId(), wishClubs);

        System.out.println(">>>>>>>>>>>> UserClub :"+user.getUserClubs());
        System.out.println(">>>>>>>>>>>> Test Succeed !");
    }

    @Test
    @Transactional
    @DisplayName("2. UserController : 가입한 동아리 조회(getJoinedClub)")
    void getJoinedTest(){
        System.out.println(">>>>>>>>>>>> Test Start.");

        User user1 =  userRepository.findByStudentId("20171700").get();
        List<ClubDto> joinedClub = userService.getJoinedClub(user1.getStudentId());
        System.out.println("< "+user1.getName()+"'s joined CLub List" + " >");
        joinedClub.forEach(System.out::println);
    }
}