package com.hoppy.app.story.controller;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoppy.app.login.WithMockCustomUser;
import com.hoppy.app.login.auth.SocialType;
import com.hoppy.app.member.Role;
import com.hoppy.app.member.domain.Member;
import com.hoppy.app.member.repository.MemberRepository;
import com.hoppy.app.story.dto.UploadStoryDto;
import com.hoppy.app.story.repository.StoryRepository;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
class StoryControllerTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    StoryRepository storyRepository;

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        memberRepository.save(
                Member.builder()
                        .username("최대한")
                        .role(Role.USER)
                        .id(8669L)
                        .profileImageUrl("https://www.image.com/test")
                        .socialType(SocialType.KAKAO)
                        .email("test99@naver.com")
                        .password("secret-key")
                        .intro("잘부탁드립니다.")
                        .build()
        );
    }

    @Test
    @WithMockCustomUser(id = "8669")
    void uploadStory() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long id = Long.parseLong(authentication.getName());
        Optional<Member> optMember = memberRepository.findById(id);
        assertThat(optMember.isPresent());

        UploadStoryDto dto = UploadStoryDto.builder().title("1th Story Test").content("1th Upload").filename("1.avi").build();
        String content = objectMapper.writeValueAsString(dto);
        ResultActions result = mvc.perform(MockMvcRequestBuilders
                .post("/story")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
        ).andDo(print());
    }
}