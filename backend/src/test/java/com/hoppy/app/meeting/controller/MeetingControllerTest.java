package com.hoppy.app.meeting.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoppy.app.community.domain.Post;
import com.hoppy.app.community.repository.PostRepository;
import com.hoppy.app.like.domain.MemberPostLike;
import com.hoppy.app.like.repository.MemberPostLikeRepository;
import com.hoppy.app.login.WithMockCustomUser;
import com.hoppy.app.login.auth.SocialType;
import com.hoppy.app.meeting.Category;
import com.hoppy.app.meeting.domain.Meeting;
import com.hoppy.app.meeting.dto.CreateMeetingDto;
import com.hoppy.app.meeting.dto.MeetingJoinDto;
import com.hoppy.app.meeting.dto.MeetingWithdrawalDto;
import com.hoppy.app.meeting.dto.UpdateMeetingDto;
import com.hoppy.app.meeting.repository.MeetingRepository;
import com.hoppy.app.meeting.service.MeetingService;
import com.hoppy.app.member.Role;
import com.hoppy.app.member.domain.Member;
import com.hoppy.app.member.domain.MemberMeeting;
import com.hoppy.app.like.domain.MemberMeetingLike;
import com.hoppy.app.like.repository.MemberMeetingLikeRepository;
import com.hoppy.app.member.repository.MemberMeetingRepository;
import com.hoppy.app.member.repository.MemberRepository;
import java.util.List;
import java.util.Optional;

import com.hoppy.app.member.service.MemberService;
import com.hoppy.app.utility.EntityUtility;
import com.hoppy.app.utility.RequestUtility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@TestInstance(Lifecycle.PER_METHOD)
@DisplayName("모임 컨트롤러 테스트")
class MeetingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MeetingService meetingService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private MemberMeetingRepository memberMeetingRepository;

    @Autowired
    private MemberMeetingLikeRepository memberMeetingLikeRepository;

    @Autowired
    private MemberPostLikeRepository memberPostLikeRepository;

    @Autowired
    private PostRepository postRepository;

    @AfterEach
    void after() {
        memberMeetingLikeRepository.deleteAll();
        memberPostLikeRepository.deleteAll();
        memberMeetingRepository.deleteAll();
        postRepository.deleteAll();
        meetingRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @BeforeEach
    void before() {
        final long MEMBER_ID = 1L;
        Member member = memberRepository.save(
                Member.builder()
                .username("tae-kyoung")
                .profileImageUrl("profile-url")
                .id(MEMBER_ID)
                .build()
        );

        for (int i = 0; i < 20; i++) {
            Meeting meeting = meetingRepository.save(Meeting.builder()
                    .owner(member)
                    .category(Category.ART)
                    .title((i + 1) + "-title")
                    .content((i + 1) + "-content")
                    .memberLimit(15)
                    .build()
            );

            memberMeetingRepository.save(MemberMeeting.builder()
                    .meeting(meeting)
                    .member(member)
                    .build()
            );

            if(i % 3 == 0) {
                memberMeetingLikeRepository.save(MemberMeetingLike.builder()
                        .meeting(meeting)
                        .member(member)
                        .build()
                );
            }
        }
    }

    @DisplayName("모임 생성 테스트")
    @Test
    @WithMockCustomUser(id = "1", password = "secret-key", role = Role.USER, socialType = SocialType.KAKAO)
    void createMeetingTest() throws Exception {

        CreateMeetingDto createMeetingDto = CreateMeetingDto.builder()
                .url("https://picsum.photos/500/500")
                .title("testTitle")
                .content("testContent")
                .memberLimit(10)
                .category(1)
                .build();

        String content = objectMapper.writeValueAsString(createMeetingDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/meeting")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("message", is("모임 생성 완료")))
                .andDo(document("create-meeting",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @DisplayName("카테고리의 모임 페이징 테스트")
    @Test
    @WithMockCustomUser(id = "1", password = "secret-key", role = Role.USER, socialType = SocialType.KAKAO)
    void getMeetingListByCategoryWithPagingTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                .get("/meeting?categoryNumber=2")
                .accept(MediaType.APPLICATION_JSON)
            )
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(document("meeting-pagination",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @DisplayName("모임 상세 조회 테스트")
    @Test
    @WithMockCustomUser(id = "1", password = "secret-key", role = Role.USER, socialType = SocialType.KAKAO)
    void getMeetingDetailTest() throws Exception {
        //given
        List<Meeting> meetingList = meetingRepository.findAll();
        assertThat(meetingList.size()).isGreaterThan(0);
        Long testMeetingId = meetingList.get(0).getId();

        //when~then
        mockMvc.perform(MockMvcRequestBuilders
                .get("/meeting/" + testMeetingId)
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.participantList[0].owner", is(Boolean.TRUE)))
                .andDo(MockMvcResultHandlers.print())
                .andDo(document("meeting-detail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @DisplayName("모임 가입 테스트")
    @Test
    @WithMockCustomUser(id = "1", password = "secret-key", role = Role.USER, socialType = SocialType.KAKAO)
    void meetingJoinTest() throws Exception {
        //given
        Member member = memberRepository.save(EntityUtility.testMember(1L));
        Meeting meeting = meetingRepository.save(EntityUtility.testHealthMeeting(member));

        MeetingJoinDto meetingJoinDto = MeetingJoinDto.builder()
                .meetingId(meeting.getId())
                .build();

        String content = objectMapper.writeValueAsString(meetingJoinDto);

        //when~then
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/meeting/entry")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(document("meeting-join",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @DisplayName("모임 탈퇴 테스트")
    @Test
    @WithMockCustomUser(id = "99", password = "secret-key", role = Role.USER, socialType = SocialType.KAKAO)
    void meetingWithdrawTest() throws Exception {
        //given
        List<Meeting> meetingList = meetingRepository.findAll();
        Meeting meeting = meetingList.get(0);
        Member member = memberRepository.save(EntityUtility.testMember(99L));
        memberMeetingRepository.save(MemberMeeting.of(member, meeting));

        MeetingWithdrawalDto meetingWithdrawalDto = MeetingWithdrawalDto.builder()
                .meetingId(meeting.getId())
                .build();

        String content = objectMapper.writeValueAsString(meetingWithdrawalDto);

        //when~then
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/meeting/withdrawal")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(document("meeting-withdraw",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @DisplayName("커뮤니티 게시글 페이징 테스트")
    @Test
    @WithMockCustomUser(id = "1", password = "secret-key", role = Role.USER, socialType = SocialType.KAKAO)
    void getPostsWithPagingTest() throws Exception {
        // given
        Optional<Member> optionalMember = memberRepository.findById(1L);
        assert optionalMember.isPresent() : "NOT_FOUND_MEMBER";

        Member author = optionalMember.get();
        Meeting meeting = meetingRepository.save(EntityUtility.testHealthMeeting(author));
        memberMeetingRepository.save(MemberMeeting.of(author, meeting));

        final int POST_COUNT = 20;
        for(int i = 0; i < POST_COUNT; i++) {
            Post post = postRepository.save(
                    Post.builder()
                            .title((i + 1) + "-title")
                            .content((i + 1) + "-content")
                            .author(author)
                            .meeting(meeting)
                            .build()
            );

            if(i % 2 == 0) {
                memberPostLikeRepository.save(
                        MemberPostLike.builder()
                                .member(author)
                                .post(post)
                                .build()
                );
            }
        }

        // when
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/meeting/posts" + "?meetingId=" + meeting.getId())
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(document("post-pagination",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @DisplayName("모임 좋아요 테스트")
    @Test
    @WithMockCustomUser(id = "1", password = "secret-key", role = Role.USER, socialType = SocialType.KAKAO)
    void meetingLikeTest() throws Exception {
        // given
        Optional<Member> optionalMember = memberRepository.findById(1L);
        assert optionalMember.isPresent() : "NOT_FOUND_MEMBER";

        Member member = optionalMember.get();
        Meeting meeting = meetingRepository.save(EntityUtility.testHealthMeeting(member));

        // when
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/meeting/like/" + meeting.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(document("meeting-like-request",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ))
                .andDo(print());

        // then
        Optional<MemberMeetingLike> opt = memberMeetingLikeRepository.findByMemberIdAndMeetingId(member.getId(), meeting.getId());
        assertThat(opt).isPresent();
    }

    @DisplayName("모임 좋아요 취소 테스트")
    @Test
    @WithMockCustomUser(id = "1", password = "secret-key", role = Role.USER, socialType = SocialType.KAKAO)
    void meetingDislikeTest() throws Exception {
        // given
        Optional<Member> optionalMember = memberRepository.findById(1L);
        assert optionalMember.isPresent() : "NOT_FOUND_MEMBER";

        Member member = optionalMember.get();
        Meeting meeting = meetingRepository.save(EntityUtility.testHealthMeeting(member));
        memberMeetingLikeRepository.save(MemberMeetingLike.of(member, meeting));

        // when
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/meeting/like/" + meeting.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(document("meeting-dislike-request",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ))
                .andDo(print());

        // then
        Optional<MemberMeetingLike> opt = memberMeetingLikeRepository.findByMemberIdAndMeetingId(member.getId(), meeting.getId());
        assertThat(opt).isEmpty();
    }

    @DisplayName("모임 수정 테스트")
    @Test
    @WithMockCustomUser(id = "1", password = "secret-key", role = Role.USER, socialType = SocialType.KAKAO)
    void meetingUpdateTest() throws Exception {
        // given
        Member member = memberService.findById(1L);
        Meeting meeting = meetingRepository.save(EntityUtility.testMeeting(member, Category.HEALTH));
        UpdateMeetingDto dto = new UpdateMeetingDto("update-title", "update-content", null);
        String content = objectMapper.writeValueAsString(dto);

        // when
        ResultActions resultActions = RequestUtility.patchRequest(mockMvc, "/meeting/" + meeting.getId(), content);

        // then
        meeting = meetingService.findById(meeting.getId());
        assertThat(meeting.getTitle()).isEqualTo("update-title");
        assertThat(meeting.getContent()).isEqualTo("update-content");
        assertThat(meeting.getUrl()).isNotNull();

        resultActions.andDo(document("meeting-update-request",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())
        ))
        .andDo(print());
    }

    @DisplayName("모임 검색 테스트")
    @Test
    @WithMockCustomUser(id = "1", password = "secret-key", role = Role.USER, socialType = SocialType.KAKAO)
    void meetingSearchTest() throws Exception {
        // given
        Member member = memberService.findById(1L);

        // when
        ResultActions resultActions = RequestUtility.getRequest(mockMvc, "/search/meeting/title");

        // then
        resultActions.andExpect(jsonPath("$.data.meetingList.size()", is(14)))
                .andDo(document("meeting-search-request",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ))
                .andDo(print());
    }
}