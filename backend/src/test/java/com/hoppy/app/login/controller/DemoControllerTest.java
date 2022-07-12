package com.hoppy.app.login.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.hoppy.app.login.WithMockCustomUser;
import com.hoppy.app.login.auth.SocialType;
import com.hoppy.app.login.auth.authentication.CustomUserDetails;
import com.hoppy.app.member.Role;
import com.hoppy.app.member.domain.Member;
import com.hoppy.app.member.dto.LoginMemberDto;
import com.hoppy.app.member.repository.MemberRepository;
import com.hoppy.app.member.service.MemberDTOService;
import com.hoppy.app.response.dto.ResponseDto;
import com.hoppy.app.response.service.SuccessCode;
import java.io.IOException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@AutoConfigureMockMvc
//@WebMvcTest
@SpringBootTest
@AutoConfigureRestDocs
class DemoControllerTest {

    @Autowired
    MockMvc mvc;

//    @Autowired
    @Mock
    private MemberRepository memberRepository;

    @Autowired
    MemberDTOService memberDTOService;

/*    @BeforeEach
    void makeVirtualMember() {
        memberRepository.save(
                Member.builder()
                        .id(1L)
                        .socialType(SocialType.KAKAO).socialId("12341234")
                        .username("마석도").email("test123@naver.com").password("98765")
                        .profileImageUrl("https://www.xxx.com//myimage").role(Role.USER).intro("반갑습니다!").build()
        );
    }*/

    @Test
    @DisplayName("현재 인증된 사용자의 SocialId로 정보 조회")
    @WithMockCustomUser(id = "98765", password = "secret", role = Role.USER, socialType = SocialType.KAKAO)
    void login() throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = authentication.getName();
        System.out.println("id = " + id);
        
        MemberRepository mr = mock(MemberRepository.class);

        Member member = Member.builder()
                .id(12341234L)
                .username("마석도")
                .email("test123@naver.com")
                .role(Role.USER)
                .socialType(SocialType.KAKAO)
                .profileImageUrl("www.image.com/myimage")
                .id(1L)
                .intro("반갑습니다. 잘부탁드립니다!")
                .password("9999")
                .build();

        when(mr.save(member)).thenReturn(new Member(1234L, "email", "testUser", "profileUrl",
                "hello", SocialType.KAKAO, Role.USER));
        when(mr.findAll()).thenReturn(Mockito.anyList());

        System.out.println("member.getUsername() = " + member.getUsername());  // "마석도"

        System.out.println("mr.save(member).getUsername() = " + mr.save(member).getUsername());  // "name"
        System.out.println("mr.findAll() = " + mr.findAll());  // []

        assertFalse(mr.save(member) == member);
        assertFalse(mr.findAll() == new Object());

        /*Optional<Member> member = memberRepository.findBySocialId(socialId);
        if(member.isEmpty()) {
            System.out.println("존재하지 않는 회원입니다.");
        }
        LoginMemberDto loginMemberDto = memberDTOService.loginSuccessResponse(member.get());
        System.out.println("loginMemberDto.getEmail() = " + loginMemberDto.getEmail());
        SuccessCode responseCode = SuccessCode.LOGIN_SUCCESS;
        ResponseEntity<ResponseDto> responseEntity = new ResponseEntity<>(
                ResponseDto.commonResponse(responseCode.getStatus(), responseCode.getMessage()),
                HttpStatus.valueOf(responseCode.getStatus()));
        System.out.println("responseEntity = " + responseEntity);
        Mockito.when(memberRepository.findBySocialId(Mockito.any(String.class)))
                .thenReturn(member);*/
    }

    @Test
    public void showMember() throws Exception {
        mvc.perform(
                get("/api/show/{id}", 123L)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andDo(
                        document
                )
    }
}