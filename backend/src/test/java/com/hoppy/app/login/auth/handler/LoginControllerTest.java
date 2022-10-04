package com.hoppy.app.login.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoppy.app.login.auth.authentication.KakaoOAuth2UserInfoDto;
import com.hoppy.app.member.domain.Member;
import com.hoppy.app.member.repository.MemberRepository;
import com.hoppy.app.utils.RequestUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.core.Is.is;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * packageName    : com.hoppy.app.login.auth.handler
 * fileName       : LoginControllerTest
 * author         : Kim
 * date           : 2022-10-04
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022-10-04        Kim       최초 생성
 */
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class LoginControllerTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @AfterEach
    void clean() {
        memberRepository.deleteAll();
    }

    @Test
    void createKakaoMember() throws Exception {

        KakaoOAuth2UserInfoDto dto = KakaoOAuth2UserInfoDto.builder()
                .socialId(9999)
                .email("test@kakao.com")
                .profileUrl(null)
                .build();

        String content = objectMapper.writeValueAsString(dto);

        RequestUtil.postRequest(mvc, "/login/kakao", content).andExpect(status().isOk());
    }

    @Test
    void updateKakaoMember() throws Exception {

        memberRepository.save(
                Member.builder()
                        .id(9999L)
                        .email("test@kakao.com")
                        .deleted(true)
                        .build()
        );

        KakaoOAuth2UserInfoDto dto = KakaoOAuth2UserInfoDto.builder()
                .socialId(9999)
                .email("test@gmail.com")
                .profileUrl(null)
                .build();

        String content = objectMapper.writeValueAsString(dto);

        ResultActions actions = RequestUtil.postRequest(mvc, "/login/kakao", content);

        Member member = memberRepository.findById(9999L).orElseGet(() -> null);
        Assertions.assertThat(member).isNotNull();
        Assertions.assertThat(member.getEmail()).isEqualTo("test@gmail.com");
        Assertions.assertThat(member.isDeleted()).isFalse();

        actions.andExpect(status().isOk())
            .andDo(document("kakao-login-request",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint())
            ));
    }
}