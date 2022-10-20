package com.hoppy.app.member.controller;


import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.core.Is.is;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoppy.app.like.domain.MemberMeetingLike;
import com.hoppy.app.like.repository.MemberMeetingLikeRepository;
import com.hoppy.app.login.WithMockCustomUser;
import com.hoppy.app.login.auth.SocialType;
import com.hoppy.app.login.auth.authentication.CustomUserDetails;
import com.hoppy.app.meeting.Category;
import com.hoppy.app.meeting.domain.Meeting;
import com.hoppy.app.meeting.repository.MeetingRepository;
import com.hoppy.app.member.Role;
import com.hoppy.app.member.domain.Member;
import com.hoppy.app.member.domain.MemberMeeting;
import com.hoppy.app.member.dto.MyProfileDto;
import com.hoppy.app.member.repository.MemberMeetingRepository;
import com.hoppy.app.member.repository.MemberRepository;
import com.hoppy.app.member.dto.UpdateMemberDto;
import com.hoppy.app.story.domain.story.Story;
import com.hoppy.app.story.repository.StoryRepository;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import javax.transaction.Transactional;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 * 오직 Controller에 대해서만 테스트를 검증할 때는 @WebMvcTest를 사용하면 되지만,
 * @Service, @Repository, JPA 등을 같이 사용해야 하는 로직은 @SpringBootTest & @AutoConfigureMockMvc 사용
 */

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@TestInstance(Lifecycle.PER_CLASS)
class MemberProfileControllerTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    MeetingRepository meetingRepository;

    @Autowired
    MemberMeetingRepository memberMeetingRepository;

    @Autowired
    MemberMeetingLikeRepository memberMeetingLikeRepository;

    @Autowired
    StoryRepository storyRepository;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

//    @MockBean // 모킹하게되면 responseService가 원래 동작대로 동작할 수 없음
//    private ResponseService responseService;

    /**
     * 이미 가입돼있는 가상의 회원
     */
    @BeforeAll
    void setup() {
        Member member = Member.builder().id(9999L).socialType(SocialType.KAKAO).username("강해상").
                email("test123@naver.com").password("secret-key").profileImageUrl("https://www.xxx.com//myimage")
                .role(Role.USER).intro("반갑습니다!")
                .build();
        memberRepository.save(member);

        for(int i = 1; i <= 10; i++) {
            Category category;
            if(i % 2 == 0) category = Category.HEALTH;
            else category = Category.LIFE;
            Meeting meeting = meetingRepository.save(Meeting.builder().owner(member).url("https://test" + i + ".com")
                    .title(i + "번 모임").content("Welcome to meeting No." + i).category(category).memberLimit(i+9).build());
            if(i % 3 == 0) {
                memberMeetingRepository.save(MemberMeeting.builder()
                        .member(member)
                        .meeting(meeting)
                        .build());
            }
            if (i % 4 == 0) {
                memberMeetingLikeRepository.save(MemberMeetingLike.builder()
                        .member(member)
                        .meeting(meeting)
                        .build());
            }
        }

        for(int i = 1; i <= 5; i++) {
            storyRepository.save(
                    Story.builder()
                            .member(member)
                            .title(i+"th Story")
                            .content("This is " + i + "th Story")
                            .filePath(i+".jpg")
                            .build()
            );
        }
    }

    @AfterAll
    void afterAll() {
        memberMeetingLikeRepository.deleteAll();
        memberMeetingRepository.deleteAll();
        meetingRepository.deleteAll();
        storyRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @WithMockCustomUser(id = "9999", password = "secret-key", role = Role.USER, socialType = SocialType.KAKAO)
    void showMyProfile() throws Exception {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        Optional<Member> optMember = memberRepository.findById(principal.getId());

        assertThat(optMember).isPresent();

        mvc.perform(MockMvcRequestBuilders.get("/profile")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.username", is(optMember.get().getUsername())))
                .andDo(document("show-myProfile",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()))
                );
    }

    @Test
    @WithMockCustomUser(id = "9999")
    void showMyStoriesInProfile() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long memberId = Long.parseLong(authentication.getName());
        Optional<Member> optMember = memberRepository.findById(memberId);
        assertThat(optMember).isPresent();
        ResultActions result = mvc.perform(
                        MockMvcRequestBuilders
                                .get("/profile/story")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                )
                .andDo(print());
        result.andExpect(status().isOk())
                .andDo(document("myprofile-story",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    void showUserProfile() throws Exception {
        String id = "9999";
        Optional<Member> optMember = memberRepository.findById(Long.parseLong(id));
        assertThat(optMember).isPresent();
        mvc.perform(MockMvcRequestBuilders.get("/profile/member")
                        .param("id", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.username", is(optMember.get().getUsername())))
                .andDo(document("show-userProfile",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    @WithMockCustomUser(id = "9999")
    void updateUser() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long id = Long.parseLong(authentication.getName());
        Optional<Member> optMember = memberRepository.findById(id);

        assertThat(optMember).isPresent();

        Member exMember = optMember.get();

        UpdateMemberDto memberDto = UpdateMemberDto.builder().username("마석도")
                .profileUrl("www.changing-url.com").intro("회원 정보 수정 테스트").build();
        String content = objectMapper.writeValueAsString(memberDto);

        ResultActions result = mvc.perform(MockMvcRequestBuilders
                        .put("/profile")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                )
                .andDo(print());

        Member updatedMember = memberRepository.findById(id).get();

        assertThat(updatedMember.getProfileImageUrl()).isNotEqualTo(exMember.getProfileImageUrl());
        assertThat(updatedMember.getUsername()).isNotEqualTo(exMember.getUsername());
        assertThat(updatedMember.getId()).isEqualTo(exMember.getId());

        result.andExpect(status().isOk())
                .andDo(document("update-user",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }
}