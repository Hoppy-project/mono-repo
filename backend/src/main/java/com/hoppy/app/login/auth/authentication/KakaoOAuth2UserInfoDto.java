package com.hoppy.app.login.auth.authentication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

/**
 * packageName    : com.hoppy.app.login.auth.authentication
 * fileName       : KakaoOAuth2UserInfoDto
 * author         : Kim
 * date           : 2022-10-04
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022-10-04        Kim       최초 생성
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KakaoOAuth2UserInfoDto {

    @NotEmpty
    private long socialId;

    private String email;
    private String profileUrl;
}
