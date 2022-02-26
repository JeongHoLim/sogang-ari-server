package com.ari.sogang.domain.service;

<<<<<<< HEAD
import com.ari.sogang.domain.dto.ClubDto;
import com.ari.sogang.domain.dto.UserWishListDto;
import com.ari.sogang.domain.entity.Club;
=======
import com.ari.sogang.domain.entity.User;
import com.ari.sogang.domain.entity.UserAuthority;
>>>>>>> 2b60ba950abe5dbba21cad46261a517cae7c194b
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

//    @Transactional(propagation = Propagation.REQUIRED)
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
