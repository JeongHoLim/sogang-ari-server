package com.ari.sogang.domain.service;

import com.ari.sogang.config.dto.ResponseDto;
import com.ari.sogang.domain.repository.ClubRepository;
import com.ari.sogang.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final ClubRepository clubRepository;
    private final ResponseDto responseDto;

    public ResponseEntity<?> registerManager(String managerId,String clubId){

        var optionalUser = userRepository.findByStudentId(managerId);
        var optionalClub = clubRepository.findById(Long.valueOf(clubId));
        if(optionalClub.isEmpty() || optionalUser.isEmpty())
            return responseDto.fail("존재하지 않는 동아리 혹은 유저",HttpStatus.NOT_FOUND);

//        var club = optionalClub.get();
        var user = optionalUser.get();

        userService.addAuthority(user.getId(),"ROLE_MANAGER");
        userRepository.save(user);


        return responseDto.success("동아리장 등록 성공", HttpStatus.CREATED);
    }
}
