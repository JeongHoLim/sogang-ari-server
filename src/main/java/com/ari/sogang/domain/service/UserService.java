package com.ari.sogang.domain.service;

import com.ari.sogang.domain.entity.User;
import com.ari.sogang.domain.entity.UserAuthority;
import com.ari.sogang.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public User save(User user){
        return userRepository.save(user);
    }


    @Transactional
    public void addAuthority(Long userId,String authority){

        userRepository.findById(userId).ifPresent(user->{
            var newAuthority = new UserAuthority(userId,authority);
            if(user.getAuthorities()==null){
                var authorities = new HashSet<UserAuthority>();
                authorities.add(newAuthority);
                user.setAuthorities(authorities);
                save(user);
            }
            else if(!user.getAuthorities().contains(newAuthority)){
                var authorities = new HashSet<UserAuthority>();
                authorities.add(newAuthority);
                authorities.addAll(authorities);
                user.setAuthorities(authorities);
                save(user);
            }
        });
    }


    public Optional<User> find(String studentId) {
        return userRepository.findByStudentId(studentId);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }
}
