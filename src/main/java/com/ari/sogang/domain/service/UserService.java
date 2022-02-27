package com.ari.sogang.domain.service;

import com.ari.sogang.domain.dto.ClubDto;
import com.ari.sogang.domain.dto.UserDto;
import com.ari.sogang.domain.entity.*;
import com.ari.sogang.domain.repository.ClubRepository;
import com.ari.sogang.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ClubRepository clubRepository;
    private final DtoServiceHelper dtoServiceHelper;

    @Override
    public UserDetails loadUserByUsername(String studentId) throws UsernameNotFoundException {
        return userRepository.findByStudentId(studentId)
                .orElseThrow(()->new UsernameNotFoundException(studentId));
    }

    @Transactional
    public ResponseEntity<UserDto> save(UserDto userDto){

        var user = dtoServiceHelper.toEntity(userDto);
        userRepository.save(user);
        // 일단 회원 가입하면 유저 권한만 승인 -> 수정 필요
        addAuthority(user.getId(),"ROLE_USER");

        // 헤더 추가
        var header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        var savedDto = dtoServiceHelper.toDto(user);

        return ResponseEntity.ok()
                .headers(header)
                .body(savedDto)
                ;

    }
    /* Wish List 저장 */
    @Transactional
    public void postWishList(String studentId, List<ClubDto> clubDtos) {
        List<UserWishClub> userWishClubs = new ArrayList<>();
        User user = userRepository.findByStudentId(studentId).get();
        Long userId = user.getId();
        /* 즐겨찾기 클럽 추가 */
        for (ClubDto temp : clubDtos) {
            var clubId = clubRepository.findByName(temp.getName()).getId();
            userWishClubs.add(new UserWishClub(userId, clubId));
        }
        /* 영속성 전이 cacade에 의해 DB 저장 */
        user.setUserWishClubs(userWishClubs);
        userRepository.save(user);
    }

    /* Wish List 조회 */
    @Transactional
    public List<ClubDto> getWishList(String studentId){
        var user = userRepository.findByStudentId(studentId).get();
        var wishList = user.getUserWishClubs();
        List<ClubDto> clubList = new ArrayList<>();

        for(UserWishClub temp : wishList){
            var clubId = temp.getClubId();
            clubList.add(dtoServiceHelper.toDto(clubRepository.findById(clubId).get()));
        }
        return clubList;
    }

    /* 가입 동아리 조회 */
    @Transactional
    public List<ClubDto> getJoinedClub(String studentId){
        var user = userRepository.findByStudentId(studentId).get();
        var userClubList = user.getUserClubs();
        List<ClubDto> clubList = new ArrayList<>();

        for(UserClub temp : userClubList){
            var clubId = temp.getClubId();
            clubList.add(dtoServiceHelper.toDto(clubRepository.findById(clubId).get()));
        }
        return clubList;
    }


    @Transactional
    public void addAuthority(Long userId,String authority){

        userRepository.findById(userId).ifPresent(user->{
            var newAuthority = new UserAuthority(userId,authority);
            if(user.getAuthorities()==null){
                var authorities = new HashSet<UserAuthority>();
                authorities.add(newAuthority);
                user.setAuthorities(authorities);
                userRepository.save(user);
            }
            else if(!user.getAuthorities().contains(newAuthority)){
                var authorities = new HashSet<UserAuthority>();
                authorities.add(newAuthority);
                authorities.addAll(authorities);
                user.setAuthorities(authorities);
                userRepository.save(user);
            }
        });
    }

    @Transactional
    public void removeAuthority(Long userId,String authority){

        userRepository.findById(userId).ifPresent(user->{
            if(user.getAuthorities() == null) return;
            var targetAuthority = new UserAuthority(user.getId(),authority);
            if(user.getAuthorities().contains(targetAuthority)) {
                user.setAuthorities(
                        user.getAuthorities().stream().filter(auth -> !auth.equals(targetAuthority))
                                .collect(Collectors.toSet())
                );
                if(user.getAuthorities().size()==0)
                    user.setAuthorities(null);
                userRepository.save(user);
            }
        });
    }

    // 회원 탈퇴
    public ResponseEntity<String> signOut(String studentId) {

        var found = userRepository.findByStudentId(studentId);

        // 헤더 추가
        var header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);

        if(found.isEmpty()){
            return ResponseEntity.badRequest().headers(header)
                    .body("가입된 학번이 아닙니다.");
        }
        var target = found.get();

        userRepository.deleteById(target.getId());

        return ResponseEntity.ok()
                .headers(header)
                .body("탈퇴 완료 되었습니다.")
                ;
    }

    // 학번 중복 확인
    public boolean checkStudentId(String studentId) {
        return userRepository.findByStudentId(studentId).isEmpty();
    }

    // 이메일 중복 확인
    public boolean checkEmail(String email) {
        return userRepository.findByEmail(email).isEmpty();
    }

}
