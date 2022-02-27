package com.ari.sogang.domain.service;

import com.ari.sogang.domain.dto.ClubDto;
import com.ari.sogang.domain.dto.HashTagDto;
import com.ari.sogang.domain.dto.UserDto;
import com.ari.sogang.domain.dto.UserWishListDto;
import com.ari.sogang.domain.entity.Club;
import com.ari.sogang.domain.entity.User;
import com.ari.sogang.domain.entity.UserAuthority;
import com.ari.sogang.domain.entity.UserWishList;
import com.ari.sogang.domain.repository.ClubRepository;
import com.ari.sogang.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String studentId) throws UsernameNotFoundException {
        return userRepository.findByStudentId(studentId)
                .orElseThrow(()->new UsernameNotFoundException(studentId));
    }

    @Transactional
    public ResponseEntity<UserDto> save(UserDto userDto){

        var user = toEntity(userDto);
        userRepository.save(user);
        // 일단 회원 가입하면 유저 권한만 승인 -> 수정 필요
        addAuthority(user.getId(),"ROLE_USER");

        // 헤더 추가
        var header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        var savedDto = toDto(user);

        return ResponseEntity.ok()
                .headers(header)
                .body(userDto)
                ;

    }

    private UserDto toDto(User user) {
        return UserDto.builder()
                .email(user.getEmail())
                .major(user.getMajor())
                .name(user.getName())
                .studentId(user.getStudentId())
                .build();

        // userWishList는?
    }

    private User toEntity(UserDto userDto) {
        return User.builder()
                .studentId(userDto.getStudentId())
                .name(userDto.getName())
                .major(userDto.getMajor())
                .enabled(true)
                .password(userDto.getPassword())
                .email(userDto.getEmail())
                .build();
    }
    @Transactional
    public List<UserWishListDto> getWishList(String studentId){
        return new ArrayList<UserWishListDto>();
    }

    @Transactional
    public List<ClubDto> getJoinedClub(String studentId){
        return new ArrayList<ClubDto>();
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

    public Optional<User> find(String studentId) {
        return userRepository.findByStudentId(studentId);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }


    public ResponseEntity<String> signOut(String studentId) {

        var found = userRepository.findByStudentId(studentId);
        if(found.isEmpty()){
//            ResponseEntity.notFound()
            // handle error
        }
        var target = found.get();

        // enabled false 처리 -> admin 계정에서 일괄적으로 삭제
        target.setEnabled(false);

        // 헤더 추가
        var header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        String message = "탈퇴 완료";

        return ResponseEntity.ok()
                .headers(header)
                .body(message)
                ;

    }
}
