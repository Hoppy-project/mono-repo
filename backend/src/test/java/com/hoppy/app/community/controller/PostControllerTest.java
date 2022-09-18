package com.hoppy.app.community.controller;

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
import com.hoppy.app.community.domain.ReReply;
import com.hoppy.app.community.domain.Reply;
import com.hoppy.app.community.dto.CreatePostDto;
import com.hoppy.app.community.dto.UpdatePostDto;
import com.hoppy.app.community.repository.PostRepository;
import com.hoppy.app.community.repository.ReReplyRepository;
import com.hoppy.app.community.repository.ReplyRepository;
import com.hoppy.app.community.service.PostService;
import com.hoppy.app.like.domain.MemberPostLike;
import com.hoppy.app.like.repository.MemberPostLikeRepository;
import com.hoppy.app.login.WithMockCustomUser;
import com.hoppy.app.login.auth.SocialType;
import com.hoppy.app.meeting.Category;
import com.hoppy.app.meeting.domain.Meeting;
import com.hoppy.app.meeting.repository.MeetingRepository;
import com.hoppy.app.member.Role;
import com.hoppy.app.member.domain.Member;
import com.hoppy.app.member.repository.MemberRepository;
import com.hoppy.app.utility.EntityUtility;
import org.junit.jupiter.api.AfterEach;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

/**
 * @author 태경 2022-08-14
 */
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@TestInstance(Lifecycle.PER_METHOD)
@DisplayName("게시물 컨트롤러 테스트")
class PostControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PostService postService;

    @Autowired
    PostRepository postRepository;

    @Autowired
    ReplyRepository replyRepository;

    @Autowired
    ReReplyRepository reReplyRepository;

    @Autowired
    MeetingRepository meetingRepository;

    @Autowired
    MemberPostLikeRepository memberPostLikeRepository;

    final long TEST_MEMBER_ID = 1L;
    final int REPLY_COUNT = 2;
    final int RE_REPLY_COUNT = 2;

    @AfterEach
    void after() {
        memberPostLikeRepository.deleteAll();
        reReplyRepository.deleteAll();
        replyRepository.deleteAll();
        postRepository.deleteAll();
        meetingRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @DisplayName("게시물 상세 조회 테스트")
    @Test
    @WithMockCustomUser(id = "1", password = "pass-word", role = Role.USER, socialType = SocialType.KAKAO)
    void getPostDetail() throws Exception {
        // given
        Member member = memberRepository.save(
                Member.builder()
                        .id(TEST_MEMBER_ID)
                        .username("testName")
                        .profileImageUrl("testProfileUrl")
                        .build()
        );
        Meeting meeting = meetingRepository.save(
                Meeting.builder()
                        .owner(member)
                        .title("title")
                        .content("content")
                        .category(Category.LIFE)
                        .memberLimit(10)
                        .build()
        );
        Post post = postRepository.save(
                Post.builder()
                        .title("0-title")
                        .content("0-content")
                        .author(member)
                        .meeting(meeting)
                        .build()
        );
        for(int j = 0; j < REPLY_COUNT; j++) {
            Reply reply = replyRepository.save(
                    Reply.builder()
                            .content(j + "-content")
                            .author(member)
                            .post(post)
                            .build()
            );
            for(int k = 0; k < RE_REPLY_COUNT; k++) {
                reReplyRepository.save(
                        ReReply.builder()
                                .content(k + "-content")
                                .author(member)
                                .reply(reply)
                                .build()
                );
            }
        }
        memberPostLikeRepository.save(
                MemberPostLike.builder()
                        .member(member)
                        .post(post)
                        .build()
        );

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/post/" + post.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("message", is("게시물 조회 완료")))
                .andExpect(jsonPath("$.data.authorName", is("testName")))
                .andExpect(jsonPath("$.data.profileImageUrl", is("testProfileUrl")))
                .andExpect(jsonPath("$.data.replyCount", is(REPLY_COUNT + (REPLY_COUNT * RE_REPLY_COUNT))))
                .andDo(document("post-detail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ))
                .andDo(print());
    }

    @DisplayName("게시물 작성 테스트")
    @Test
    @WithMockCustomUser(id = "1", password = "pass-word", role = Role.USER, socialType = SocialType.KAKAO)
    void createPostTest() throws Exception {
        // given
        Member member = memberRepository.save(EntityUtility.testMember(TEST_MEMBER_ID));
        Meeting meeting = meetingRepository.save(EntityUtility.testArtMeeting(member));
        CreatePostDto createPostDto = CreatePostDto.builder()
                .meetingId(meeting.getId())
                .title("test-title")
                .content("test-content")
                .url("https://picsum.photos/500/500")
                .build();

        String content = objectMapper.writeValueAsString(createPostDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/post")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("message", is("게시물 생성 완료")))
                .andDo(document("create-post",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @DisplayName("게시물 좋아요 컨트롤러 테스트")
    @Test
    @WithMockCustomUser(id = "1", password = "pass-word", role = Role.USER, socialType = SocialType.KAKAO)
    void likePostTest() throws Exception {
        // given
        Member member = memberRepository.save(EntityUtility.testMember(TEST_MEMBER_ID));
        Post post = postRepository.save(EntityUtility.testPost(member));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/post/like/" + post.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(document("post-like-request",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ))
                .andDo(print());
    }

    @DisplayName("게시물 좋아요 취소 컨트롤러 테스트")
    @Test
    @WithMockCustomUser(id = "1", password = "pass-word", role = Role.USER, socialType = SocialType.KAKAO)
    void dislikePostTest() throws Exception {
        // given
        Member member = memberRepository.save(EntityUtility.testMember(TEST_MEMBER_ID));
        Post post = postRepository.save(EntityUtility.testPost(member));
        memberPostLikeRepository.save(MemberPostLike.of(member, post));

        // when
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/post/like/" + post.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(document("post-dislike-request",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ))
                .andDo(print());

        // then
        Optional<MemberPostLike> opt = memberPostLikeRepository.findByMemberIdAndPostId(member.getId(), post.getId());
        assertThat(opt).isEmpty();
    }

    @DisplayName("게시물 삭제 컨트롤러 테스트")
    @Test
    @WithMockCustomUser(id = "1", password = "pass-word", role = Role.USER, socialType = SocialType.KAKAO)
    void deletePostTest() throws Exception {
        // given
        Member member = memberRepository.save(EntityUtility.testMember(TEST_MEMBER_ID));
        Post post = postRepository.save(EntityUtility.testPost(member));

        // when
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/post/" + post.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("message", is("게시물 제거 완료")))
                .andDo(document("post-delete-request",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ))
                .andDo(print());

        // then
        Optional<Post> opt = postRepository.findByIdAndAuthorId(post.getId(), member.getId());
        assertThat(opt).isEmpty();
    }

    @DisplayName("게시물 수정 컨트롤러 테스트")
    @Test
    @WithMockCustomUser(id = "1", password = "pass-word", role = Role.USER, socialType = SocialType.KAKAO)
    void patchPostTest() throws Exception {
        // given
        Member member = memberRepository.save(EntityUtility.testMember(TEST_MEMBER_ID));
        Post post = postRepository.save(EntityUtility.testPost(member));

        UpdatePostDto updatePostDto = new UpdatePostDto("update", null, null);
        String content = objectMapper.writeValueAsString(updatePostDto);

        // when
        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/post/" + post.getId())
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("message", is("게시물 수정 완료")))
                .andDo(document("post-update-request",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ))
                .andDo(print());

        // then
        post = postService.findById(post.getId());
        assertThat(post.getTitle()).isEqualTo("update");
    }
}