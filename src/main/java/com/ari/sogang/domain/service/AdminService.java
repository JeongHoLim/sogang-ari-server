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

    public ResponseEntity<?> registerManager(String managerId,Long clubId){

        var optionalUser = userRepository.findByUserId(managerId);
        var optionalClub = clubRepository.findById(clubId);
        if(optionalClub.isEmpty() || optionalUser.isEmpty())
            return responseDto.fail("존재하지 않는 동아리 혹은 유저",HttpStatus.NOT_FOUND);

        var user = optionalUser.get();

        if(userService.addAuthority(user.getId(),"ROLE_MANAGER",clubId)) {
            userRepository.save(user);
            return responseDto.success("동아리장 등록 성공", HttpStatus.CREATED);
        }
        return responseDto.fail("동아리장 등록 실패",HttpStatus.BAD_REQUEST);
    }
}
