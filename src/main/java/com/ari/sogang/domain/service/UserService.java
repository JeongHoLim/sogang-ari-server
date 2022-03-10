package com.ari.sogang.domain.service;

import com.ari.sogang.config.dto.LoginResponseDto;
import com.ari.sogang.config.dto.ResponseDto;
import com.ari.sogang.config.dto.LoginRequestDto;
import com.ari.sogang.config.dto.TokenDto;
import com.ari.sogang.config.jwt.JwtTokenProvider;
import com.ari.sogang.domain.dto.ClubDto;
import com.ari.sogang.domain.dto.PasswordDto;
import com.ari.sogang.domain.dto.UserDto;
import com.ari.sogang.domain.dto.WishClubDto;
import com.ari.sogang.domain.entity.*;
import com.ari.sogang.domain.repository.ClubRepository;
import com.ari.sogang.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.concurrent.TimeUnit;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    @PersistenceContext
    private EntityManager em;

    private final UserRepository userRepository;
    private final ClubRepository clubRepository;
    private final DtoServiceHelper dtoServiceHelper;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final ResponseDto responseDto;
    private final LoginResponseDto loginResponseDto;
    private final RedisTemplate<String, String> redisTemplate;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

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

    /* 회원 가입 */
    @Transactional
    public ResponseEntity<?> save(UserDto userDto){

        if(!isValidStudentId(userDto.getStudentId()) || !isValidEmail(userDto.getEmail())){
            return responseDto.fail("해당 정보로 가입된 계정이 존재합니다.",HttpStatus.CONFLICT);
        }

        var user = dtoServiceHelper.toEntity(userDto);
        userRepository.save(user);
        // 일단 회원 가입하면 유저 권한만 승인 -> 수정 필요
        addAuthority(user.getId(),"ROLE_USER",-1L);

        return responseDto.success("회원 가입이 완료되었습니다.", HttpStatus.CREATED);

    }

    public ResponseEntity<?> login(LoginRequestDto loginRequestDto) {

        var authenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequestDto.getStudentId(), loginRequestDto.getPassword());

        if(!userRepository.existsByStudentId(loginRequestDto.getStudentId()))
            return responseDto.fail("USER_NOT_EXIST",HttpStatus.NOT_FOUND);

        // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        // authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        var refreshToken = JwtTokenProvider.makeRefreshToken((User)authentication.getPrincipal());
        var user = (User)authentication.getPrincipal();

        // 4. 로그인 성공시에 client에 전달할 정보 생성
        //4-1. userInfo
        UserDto userInfo = dtoServiceHelper.toDto(user);

        // 4-1-1. 가입한 동아리
        var joinedClub = findClubs(user);
        List<String> clubNames = new ArrayList<>();
        for(ClubDto joined : joinedClub){
            clubNames.add(joined.getName());
        }
        userInfo.setJoinedClubs(clubNames);
        // 4-1-2. 담아놓은 동아리
        var wishClub = findWishClubs(user);
        List<WishClubDto> wishClubDtos = new ArrayList<>();
        for(ClubDto wished : wishClub){
            wishClubDtos.add(
                    WishClubDto.builder()
                            .name(wished.getName())
                            .section(wished.getSection())
                            .recruiting(wished.isRecruiting())
                            .build()
            );
        }
        userInfo.setWishClubs(wishClubDtos);

        // 4-2. tokenInfo
        TokenDto tokens = TokenDto.builder()
                .accessToken(JwtTokenProvider.makeAccessToken(user))
                .refreshToken(refreshToken)
                        .build();
        // 4-3. Login Response Dto에 저장.
        LoginResponseDto loginResponseDto = LoginResponseDto.builder()
                .userInfo(userInfo)
                .tokenInfo(tokens)
                .build();

        // 5. RefreshToken Redis 저장 (expirationTime 설정을 통해 자동 삭제 처리)
        redisTemplate.opsForValue()
                .set(user.getStudentId(), refreshToken,JwtTokenProvider.REFRESH_TIME
                        ,TimeUnit.SECONDS);

        return responseDto.success(loginResponseDto,"로그인 성공");
    }

    public ResponseEntity<?> reissue(TokenDto tokenDto) {

        var refreshToken = tokenDto.getRefreshToken();
        var refreshTokenInfo = JwtTokenProvider.verfiy(refreshToken);

        String isLogout = redisTemplate.opsForValue().get(refreshToken);
        // 로그아웃 되어있는 토큰인지 검사
        if (!ObjectUtils.isEmpty(isLogout) ||  !refreshTokenInfo.isSuccess())
            return responseDto.fail("토큰 에러, 재로그인 필요",HttpStatus.BAD_REQUEST);

        if(redisTemplate.opsForValue().get(refreshTokenInfo.getStudentId())!=null){
            redisTemplate.delete(refreshTokenInfo.getStudentId());
        }

        var user = User.builder().studentId(refreshTokenInfo.getStudentId()).build();

        var tokens = TokenDto.builder()
                .accessToken(JwtTokenProvider.makeAccessToken(user))
                .refreshToken(JwtTokenProvider.makeRefreshToken(user))
                .build();

        // 기존에 사용되던 refresh 토큰 black list에 추가
        redisTemplate.opsForValue()
                .set(refreshToken,"logout",JwtTokenProvider.REFRESH_TIME,TimeUnit.SECONDS);

        return responseDto.success(tokens,"토큰 발행 성공",HttpStatus.CREATED);
    }



    /* 로그아웃 */

    public ResponseEntity<?> logout(TokenDto userLogoutForm) {
        var accessTokenInfo = JwtTokenProvider.verfiy(userLogoutForm.getAccessToken());
        var refreshTokenInfo = JwtTokenProvider.verfiy(userLogoutForm.getRefreshToken());

        if(!accessTokenInfo.isSuccess() || !refreshTokenInfo.isSuccess())
            return responseDto.fail("잘못된 요청",HttpStatus.BAD_REQUEST);

        // redis에서 refreshToken 지우기
        if(redisTemplate.opsForValue().get(refreshTokenInfo.getStudentId())!=null){
            redisTemplate.delete(accessTokenInfo.getStudentId());
        }

        // redis black list에 추가
        redisTemplate.opsForValue()
                .set(userLogoutForm.getRefreshToken(), "logout", JwtTokenProvider.REFRESH_TIME, TimeUnit.SECONDS);

        redisTemplate.opsForValue()
                .set(userLogoutForm.getAccessToken(), "logout", JwtTokenProvider.ACCESS_TIME, TimeUnit.SECONDS);


        return responseDto.success("로그아웃 성공");
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

        userWishClubs.add(new UserWishClub(userId,clubId));
        /* 영속성 전이 cacade에 의해 DB 저장 */
        user.setUserWishClubs(userWishClubs);
        userRepository.save(user);

        return responseDto.success(userWishClubs,"담아 놓기 성공",HttpStatus.CREATED);
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

        List<ClubDto> clubList = findWishClubs(user);

        return responseDto.success(clubList,"담아놓기 조회 성공");
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

    /* 학번 중복 확인 */
    public ResponseEntity<?> checkStudentId(String studentId) {
        if(isValidStudentId(studentId)) return responseDto.success("사용 가능한 학번입니다.");
        else return responseDto.fail("해당 학번으로 가입된 계정이 있습니다.",HttpStatus.CONFLICT);
    }

    private boolean isValidStudentId(String studentId){
        return userRepository.findByStudentId(studentId).isEmpty();
    }




    /* 이메일 중복 확인 */

    public ResponseEntity<?> checkEmail(String email) {
        if(isValidEmail(email)) return responseDto.success("사용 가능한 이메일입니다.");
        else return responseDto.fail("해당 이메일로 가입된 계정이 존재합니다.",HttpStatus.CONFLICT);
    }
    public boolean isValidEmail(String email) {
        return userRepository.findByEmail(email+"@sogang.ac.kr").isEmpty();
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
