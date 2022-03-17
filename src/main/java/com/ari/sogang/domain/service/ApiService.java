package com.ari.sogang.domain.service;

import com.ari.sogang.config.dto.LoginRequestDto;
import com.ari.sogang.config.dto.LoginResponseDto;
import com.ari.sogang.config.dto.ResponseDto;
import com.ari.sogang.config.dto.TokenDto;
import com.ari.sogang.config.jwt.JwtTokenProvider;
import com.ari.sogang.domain.dto.ClubDto;
import com.ari.sogang.domain.dto.UserDto;
import com.ari.sogang.domain.entity.User;
import com.ari.sogang.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
@RequiredArgsConstructor
public class ApiService {

    private final UserRepository userRepository;
    private final ResponseDto responseDto;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final DtoServiceHelper dtoServiceHelper;
    private final UserService userService;
    private final RedisTemplate<String, String> redisTemplate;


    /* 회원 가입 */
    @Transactional
    public ResponseEntity<?> save(UserDto userDto){

        if(!userService.isValidStudentId(userDto.getStudentId()) || !userService.isValidEmail(userDto.getEmail())){
            return responseDto.fail("해당 정보로 가입된 계정이 존재합니다.",HttpStatus.CONFLICT);
        }

        var user = dtoServiceHelper.toEntity(userDto);
        userRepository.save(user);
        // 일단 회원 가입하면 유저 권한만 승인 -> 수정 필요
        userService.addAuthority(user.getId(),"ROLE_USER",-1L);

        return responseDto.success("회원 가입이 완료되었습니다.", HttpStatus.CREATED);

    }

    public ResponseEntity<?> login(LoginRequestDto loginRequestDto) {

        var authenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequestDto.getStudentId(), loginRequestDto.getPassword());

        if(!userRepository.existsByStudentId(loginRequestDto.getStudentId()))
            return responseDto.fail("USER_NOT_EXIST", HttpStatus.NOT_FOUND);

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
        var joinedClub = userService.findClubs(user);
        List<String> clubNames = new ArrayList<>();
        for(ClubDto joined : joinedClub){
            clubNames.add(joined.getName());
        }
        userInfo.setJoinedClubs(clubNames);

        // 4-1-2. 담아놓은 동아리  front가 필요없다고 함
//        var wishClub = findWishClubs(user);
//        List<WishClubDto> wishClubDtos = new ArrayList<>();
//        for(ClubDto wished : wishClub){
//            wishClubDtos.add(
//                    WishClubDto.builder()
//                            .name(wished.getName())
//                            .section(wished.getSection())
//                            .recruiting(wished.isRecruiting())
//                            .build()
//            );
//        }
//        userInfo.setWishClubs(wishClubDtos);

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
                        , TimeUnit.SECONDS);

        return responseDto.success(loginResponseDto,"로그인 성공");
    }


    /* 로그아웃 */

    public ResponseEntity<?> logout(TokenDto userLogoutForm) {
        var accessTokenInfo = JwtTokenProvider.verfiy(userLogoutForm.getAccessToken());
        var refreshTokenInfo = JwtTokenProvider.verfiy(userLogoutForm.getRefreshToken());

        String isLogoutAccess = redisTemplate.opsForValue().get(userLogoutForm.getAccessToken());
        String isLogoutRefresh = redisTemplate.opsForValue().get(userLogoutForm.getRefreshToken());

        if(!ObjectUtils.isEmpty(isLogoutAccess)|| !ObjectUtils.isEmpty(isLogoutRefresh) ||
                !accessTokenInfo.isSuccess() || !refreshTokenInfo.isSuccess()) {
            return responseDto.fail("잘못된 요청", HttpStatus.BAD_REQUEST);
        }

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

    /* 학번 중복 확인 */
    public ResponseEntity<?> checkStudentId(String studentId) {
        if(userService.isValidStudentId(studentId)) return responseDto.success("사용 가능한 학번입니다.");
        else return responseDto.fail("해당 학번으로 가입된 계정이 있습니다.",HttpStatus.CONFLICT);
    }


    /* 이메일 중복 확인 */

    public ResponseEntity<?> checkEmail(String email) {
        if(userService.isValidEmail(email)) return responseDto.success("사용 가능한 이메일입니다.");
        else return responseDto.fail("해당 이메일로 가입된 계정이 존재합니다.",HttpStatus.CONFLICT);
    }




}
