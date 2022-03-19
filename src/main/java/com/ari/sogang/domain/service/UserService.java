package com.ari.sogang.domain.service;

import com.ari.sogang.config.dto.ResponseDto;
import com.ari.sogang.domain.dto.ClubDto;
import com.ari.sogang.domain.dto.PasswordDto;
import com.ari.sogang.domain.dto.WishClubDto;
import com.ari.sogang.domain.entity.*;
import com.ari.sogang.domain.repository.ClubRepository;
import com.ari.sogang.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ClubRepository clubRepository;
    private final DtoServiceHelper dtoServiceHelper;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final ResponseDto responseDto;

    private final char[] passwordTable =  { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
            'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
            'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
            'w', 'x', 'y', 'z', '!', '@', '#', '$', '%', '^', '&', '*',
            '(', ')', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0' };


    @Override
    public UserDetails loadUserByUsername(String studentId) throws UsernameNotFoundException {
        return userRepository.findByStudentId(studentId)
                .orElseThrow(()->new UsernameNotFoundException(studentId));
    }


    protected boolean isValidStudentId(String studentId){
        return userRepository.findByStudentId(studentId).isEmpty();
    }


    /* Wish List 추가 */
    @Transactional
    public ResponseEntity<?> postWishList(String studentId, Long clubId) {
        var optionalUser = userRepository.findByStudentId(studentId);
        if(optionalUser.isEmpty()) return responseDto.fail("USER_NOT_EXIST",HttpStatus.NOT_FOUND);
        var user = optionalUser.get();
        List<UserWishClub> userWishClubs = user.getUserWishClubs();
        Long userId = user.getId();

        var optionalClub = clubRepository.findById(clubId);
        if(optionalClub.isEmpty()) return responseDto.fail("CLUB_NOT_EXIST",HttpStatus.NOT_FOUND);
        var club = optionalClub.get();
        var newClub = new UserWishClub(userId,clubId);

        if(user.getUserWishClubs().contains(newClub))
            return responseDto.fail("이미 담아놓은 동아리입니다.",HttpStatus.CONFLICT);

        userWishClubs.add(newClub);
        /* 영속성 전이 cacade에 의해 DB 저장 */
        user.setUserWishClubs(userWishClubs);
        userRepository.save(user);


        var newWishClubDto = WishClubDto.builder()
                .clubId(newClub.getClubId())
                .section(club.getSection())
                .name(club.getName())
                .recruiting(club.isRecruiting())
                .build();

        return responseDto.success(newWishClubDto,"담아 놓기 성공",HttpStatus.CREATED);
    }

    /* 담아놓은 동아리 조회하는 함수 */
    public List<ClubDto> findWishClubs(User user){
        var wishList = user.getUserWishClubs();
        List<ClubDto> result = new ArrayList<>();
        for(UserWishClub temp : wishList){
            var clubId = temp.getClubId();
            result.add(dtoServiceHelper.toDto(clubRepository.findById(clubId).get()));
        }
        return result;
    }
    /* Wish List 조회 */
    @Transactional
    public ResponseEntity<?> getWishList(String studentId){
        var optionalUser = userRepository.findByStudentId(studentId);
        if(optionalUser.isEmpty()) return responseDto.fail("USER_NOT_EXIST",HttpStatus.NOT_FOUND);
        var user = optionalUser.get();

        var wishClub = findWishClubs(user);
        List<WishClubDto> wishClubDtos = new ArrayList<>();
        for(ClubDto wished : wishClub){
            wishClubDtos.add(
                    WishClubDto.builder()
                            .clubId(wished.getId())
                            .name(wished.getName())
                            .section(wished.getSection())
                            .recruiting(wished.isRecruiting())
                            .build()
            );
        }


        return responseDto.success(wishClubDtos,"담아놓기 조회 성공");
    }

    /* 가입한 동아리 조회하는 함수 */
    public List<ClubDto> findClubs(User user){
        var userClubList = user.getUserClubs();
        List<ClubDto> result = new ArrayList<>();
        for(UserClub temp : userClubList){
            var clubId = temp.getClubId();
            result.add(dtoServiceHelper.toDto(clubRepository.findById(clubId).get()));
        }
        return result;
    }

    /* 가입 동아리 조회 */
    @Transactional
    public ResponseEntity<?> getJoinedClub(String studentId){
        var optionalUser = userRepository.findByStudentId(studentId);
        if(optionalUser.isEmpty()) return responseDto.fail("USER_NOT_EXIST",HttpStatus.NOT_FOUND);
        var user = optionalUser.get();

        List<ClubDto> clubList = findClubs(user);

        return responseDto.success(clubList,"가입된 동아리 조회 성공");
    }

    /* 권한 부여 */
    public boolean addAuthority(Long userId,String authority,Long clubId) {

        var optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            var user = optionalUser.get();
            var newAuthority = new UserAuthority(userId, authority, clubId);
            if (!user.getAuthorities().contains(newAuthority)) {
                user.getAuthorities().add(newAuthority);
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }
    /* 권한 제거 */
    public boolean removeAuthority(Long userId,String authority,Long clubId){

        var optionalUser = userRepository.findById(userId);

        if(optionalUser.isPresent()){
            var user = optionalUser.get();
            var targetAuthority = new UserAuthority(userId,authority,clubId);

            if(!user.getAuthorities().contains(targetAuthority)) {
                return false;
            }

            user.getAuthorities().remove(targetAuthority);

            userRepository.save(user);
            return true;
        }
        return false;
    }


    /* 회원 탈퇴 */
    @Transactional
    public ResponseEntity<?> signOut(String studentId) {

        var optionalUser = userRepository.findByStudentId(studentId);

        if(optionalUser.isEmpty()){
            return responseDto.fail("USER_NOT_EXIST",HttpStatus.NOT_FOUND);
        }
        var target = optionalUser.get();

        userRepository.deleteById(target.getId());

        return responseDto.success("탈퇴 성공");
    }








    /* 비밀번호 리셋 */

    @Transactional
    public ResponseEntity<?> resetPassword(String studentId) {

        var optionalUser = userRepository.findByStudentId(studentId);
        if(optionalUser.isEmpty()) return responseDto.fail("USER_NOT_EXIST",HttpStatus.NOT_FOUND);

        var user = optionalUser.get();


        var newPassword = generatePassword();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        emailService.sendPassword(user,newPassword);

        return responseDto.success("비밀번호 변경 성공");
    }


    /* 비밀번호 변경 */

    @Transactional
    public ResponseEntity<?> changePassword(String studentId, PasswordDto passwordDto) {
        var optionalUser = userRepository.findByStudentId(studentId);
        if(optionalUser.isEmpty())
            return responseDto.fail("USER_NOT_EXIST",HttpStatus.NOT_FOUND);

        var user = optionalUser.get();
        if(passwordEncoder.matches(passwordDto.getOldPassword(),user.getPassword())
                && passwordDto.getNewPassword().equals(passwordDto.getCheckPassword())){

            user.setPassword(passwordEncoder.encode(passwordDto.getNewPassword()));
            userRepository.save(user);

            return responseDto.success("비밀번호 변경 성공");
        }
        return responseDto.fail("비밀번호 변경 실패",HttpStatus.BAD_REQUEST);
    }


    /* 임시 비밀번호 생성 */
    private String generatePassword() {

        Random random = new Random(System.currentTimeMillis());
        int tableLength = passwordTable.length;
        StringBuilder buf = new StringBuilder();

        for(int i = 0; i < 8; i++) {
            buf.append(passwordTable[random.nextInt(tableLength)]);
        }

        return buf.toString();
    }

    /* Wish List 삭제 */
    public ResponseEntity<?> updateWishList(String studentId, Long clubId) {

        var optionalUser = userRepository.findByStudentId(studentId);

        if(optionalUser.isEmpty()) return responseDto.fail("USER_NOT_EXIST",HttpStatus.NOT_FOUND);
        User user = optionalUser.get();

        var optionalClub = clubRepository.findById(clubId);
        if(optionalClub.isEmpty()) return responseDto.fail("CLUB_NOT_EXIST",HttpStatus.NOT_FOUND);

        boolean success = false;
        /*해당되는 User_Wish_List entity 레코드 삭제*/

        var found = user.getUserWishClubs();
        for(var x : found){
            if(Objects.equals(x.getClubId(), clubId)) {
                success = user.getUserWishClubs().remove(x);
                break;
            }
        }
        if(!success)
            return responseDto.fail("담아놓기 목록에 존재하지 않습니다.",HttpStatus.NOT_FOUND);

        userRepository.save(user);

        return responseDto.success("담아놓기 업데이트 성공");
    }

    @Transactional
    public void addAdmin(){
        var user = userRepository.findByStudentId("17").get();
        addAuthority(user.getId(),"ROLE_ADMIN", 0L);
        removeAuthority(user.getId(),"ROLE_USER",-1L);
    }

    @Transactional
    public ResponseEntity<?> joinClub(String studentId, Long clubId) {

        var optionalUser = userRepository.findByStudentId(studentId);
        var optionalClub = clubRepository.findById(clubId);

        if(optionalUser.isEmpty())
            return responseDto.fail("USER_NOT_EXIST",HttpStatus.NOT_FOUND);

        if(optionalClub.isEmpty())
            return responseDto.fail("CLUB_NOT_EXIST",HttpStatus.NOT_FOUND);

        var club = optionalClub.get();

        var clubUsers = club.getClubUsers();
        var user = new ClubUser(clubId,optionalUser.get().getId()) ;
        if(clubUsers.contains(user)){
            return responseDto.fail("이미 가입된 동아리입니다.",HttpStatus.CONFLICT);
        }

        clubUsers.add(user);

        return responseDto.success("동아리 가입 신청 성공",HttpStatus.CREATED);
    }
}