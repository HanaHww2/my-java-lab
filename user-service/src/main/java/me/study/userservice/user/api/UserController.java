package me.study.userservice.user.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.study.userservice.common.CommonResult;
import me.study.userservice.exception.DuplicateUserMailException;
import me.study.userservice.security.JwtAuthenticationFilter;
import me.study.userservice.user.domain.User;
import me.study.userservice.user.dto.SigninReqDto;
import me.study.userservice.user.dto.SigninResDto;
import me.study.userservice.user.dto.UserDto;
import me.study.userservice.user.service.UserService;
import me.study.userservice.utils.JwtUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final JwtUtil jwtUtil;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserService userService;

    @PostMapping("/auth/signup")
    public CommonResult<User> signUpNewUser(@Valid @RequestBody UserDto userDto) throws DuplicateUserMailException {
        User user = userService.register(userDto);
        return new CommonResult<User>("User Registered Successfully", user);
    }

    @PostMapping(value = "/auth/signin")
    public ResponseEntity<CommonResult<SigninResDto>> signIn(@Valid @RequestBody SigninReqDto signinReqDto) {

        // 인증 매니저를 통해서 입력받은 아이디와 비밀번호를 바탕으로 사용자 정보를 검증한다.
        // 아래와 같이 기본 아이디/비밀번호 인증의 경우에는
        // DaoAuthenticationProvider가 디폴트로 동작하고, 이 때 커스텀해 둔 userDetailService의 loadUserByUsername()이 작동한다.
        // 즉, 최초 토큰을 발급받기 위해서는 db에서 user 정보를 가져와 인증을 수행한다.
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(
                new UsernamePasswordAuthenticationToken(
                        signinReqDto.getEmail(),
                        signinReqDto.getPassword()));

        log.info("{}", authentication.getPrincipal());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 헤더에 토큰을 담는다.
        // 경우에 따라 쿠키에 담거나, url 파라미터로 전송할 수도 있다.
        // 모바일 환경과 같이 쿠키 사용이 어려운 경우 후자를 활용한다고 한다. (헤더를 쓰는 게 낫지 않은지 확인해볼 필요가 있다.)
        String accessToken = jwtUtil.createAccessToken(authentication);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtUtil.AUTHORIZATION_HEADER, "Bearer " + accessToken);

        SigninResDto signinResDto = SigninResDto.builder()
                .email(signinReqDto.getEmail())
                .build();

        return new ResponseEntity<>(new CommonResult<>("Signed in Successfully", signinResDto), httpHeaders, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/users/me")
    public User getCurrentUserInfo(Principal principal) {
        return userService.getUser(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException(principal.getName()));
    }
}
