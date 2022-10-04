package com.hoppy.app.login.auth.handler;

import com.hoppy.app.login.auth.authentication.KakaoOAuth2UserInfoDto;
import com.hoppy.app.login.auth.provider.AuthTokenProvider;
import com.hoppy.app.login.auth.service.CustomOAuth2UserService;
import com.hoppy.app.member.domain.Member;
import com.hoppy.app.response.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * packageName    : com.hoppy.app.login.controller
 * fileName       : loginController
 * author         : Kim
 * date           : 2022-10-04
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022-10-04        Kim       최초 생성
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final AuthTokenProvider tokenProvider;

    @PostMapping("/kakao")
    public String createKakaoMember(
            @RequestBody KakaoOAuth2UserInfoDto dto
    ) {
        Member member = customOAuth2UserService.loginProcess(dto);
        String token = tokenProvider.createUserAuthToken(member.getId().toString()).getToken();

        return token;
    }
}
