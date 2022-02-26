package com.ari.sogang.domain.service;

import com.ari.sogang.domain.dto.ClubDto;
import com.ari.sogang.domain.dto.UserWishListDto;
import com.ari.sogang.domain.entity.Club;
import com.ari.sogang.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private Club clubDtoToEntity(){
        var entity = new Club();
        return entity;
    }

    @Transactional
    public List<UserWishListDto> findWishList(String id){

    }
    @Override
    public UserDetails loadUserByUsername(String studentId) throws UsernameNotFoundException {
        return userRepository.findByStudentId(studentId).orElseThrow(()->new UsernameNotFoundException(studentId));
    }
}
