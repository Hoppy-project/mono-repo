package com.hoppy.app.community.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoppy.app.community.domain.Post;
import com.hoppy.app.community.domain.ReReply;
import com.hoppy.app.community.domain.Reply;
import com.hoppy.app.community.dto.CreateReReplyDto;
import com.hoppy.app.community.dto.CreateReplyDto;
import com.hoppy.app.community.dto.UpdateReplyDto;
import com.hoppy.app.community.repository.PostRepository;
import com.hoppy.app.community.repository.ReReplyRepository;
import com.hoppy.app.community.repository.ReplyRepository;
import com.hoppy.app.community.service.ReplyService;
import com.hoppy.app.like.domain.MemberReReplyLike;
import com.hoppy.app.like.domain.MemberReplyLike;
import com.hoppy.app.like.repository.MemberReReplyLikeRepository;
import com.hoppy.app.like.repository.MemberReplyLikeRepository;
import com.hoppy.app.login.WithMockCustomUser;
import com.hoppy.app.login.auth.SocialType;
import com.hoppy.app.member.Role;
import com.hoppy.app.member.domain.Member;
import com.hoppy.app.member.repository.MemberRepository;
import com.hoppy.app.utils.EntityUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Optional;

import static com.hoppy.app.utils.RequestUtil.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * packageName    : com.hoppy.app.community.controller
 * fileName       : ReplyControllerTest
 * author         : Kim
 * date           : 2022-08-29
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022-08-29        Kim       최초 생성
 * 2022-09-02        Kim       업데이트 로직 테스트 작성
 *                             RequestUtility 생성
 */
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@DisplayName("댓글 컨트롤러 테스트")
class ReplyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ReplyService replyService;

    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private ReReplyRepository reReplyRepository;

    @Autowired
    private MemberReplyLikeRepository memberReplyLikeRepository;

    @Autowired
    private MemberReReplyLikeRepository memberReReplyLikeRepository;

    @AfterEach
    void after() {
        memberReReplyLikeRepository.deleteAll();
        memberReplyLikeRepository.deleteAll();
        reReplyRepository.deleteAll();
        replyRepository.deleteAll();
        postRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @WithMockCustomUser(id = "1", password = "secret-key", role = Role.USER, socialType = SocialType.KAKAO)
    void likeReply() throws Exception {
        // given
        final long TEST_MEMBER_ID = 1L;
        Member member = memberRepository.save(EntityUtil.testMember(TEST_MEMBER_ID));
        Reply reply = replyRepository.save(EntityUtil.testReply(member));

        // when
        ResultActions result = getRequest(mockMvc, "/reply/like/" + reply.getId());
        createDocument(result, "reply-like-request");

        // then
        Optional<MemberReplyLike> opt = memberReplyLikeRepository.findByMemberIdAndReplyId(member.getId(), reply.getId());
        assertThat(opt).isPresent();
    }

    @Test
    @WithMockCustomUser(id = "1", password = "secret-key", role = Role.USER, socialType = SocialType.KAKAO)
    void dislikeReply() throws Exception {
        // given
        final long TEST_MEMBER_ID = 1L;
        Member member = memberRepository.save(EntityUtil.testMember(TEST_MEMBER_ID));
        Reply reply = replyRepository.save(EntityUtil.testReply(member));
        memberReplyLikeRepository.save(MemberReplyLike.of(member, reply));

        // when
        ResultActions result = deleteRequest(mockMvc, "/reply/like/" + reply.getId());
        createDocument(result, "reply-dislike-request");

        // then
        Optional<MemberReplyLike> opt = memberReplyLikeRepository.findByMemberIdAndReplyId(member.getId(), reply.getId());
        assertThat(opt).isEmpty();
    }

    @Test
    @WithMockCustomUser(id = "1", password = "secret-key", role = Role.USER, socialType = SocialType.KAKAO)
    void likeReReply() throws Exception {
        // given
        final long TEST_MEMBER_ID = 1L;
        Member member = memberRepository.save(EntityUtil.testMember(TEST_MEMBER_ID));
        ReReply reReply = reReplyRepository.save(EntityUtil.testReReply(member));

        // when
        ResultActions result = getRequest(mockMvc, "/reply/re/like/" + reReply.getId());
        createDocument(result, "reReply-like-request");

        // then
        Optional<MemberReReplyLike> opt = memberReReplyLikeRepository.findByMemberIdAndReplyId(member.getId(), reReply.getId());
        assertThat(opt).isPresent();
    }

    @Test
    @WithMockCustomUser(id = "1", password = "secret-key", role = Role.USER, socialType = SocialType.KAKAO)
    void dislikeReReply() throws Exception {
        // given
        final long TEST_MEMBER_ID = 1L;
        Member member = memberRepository.save(EntityUtil.testMember(TEST_MEMBER_ID));
        ReReply reReply = reReplyRepository.save(EntityUtil.testReReply(member));
        memberReReplyLikeRepository.save(MemberReReplyLike.of(member, reReply));

        // when
        ResultActions result = deleteRequest(mockMvc, "/reply/re/like/" + reReply.getId());
        createDocument(result, "reReply-dislike-request");

        // then
        Optional<MemberReReplyLike> opt = memberReReplyLikeRepository.findByMemberIdAndReplyId(member.getId(), reReply.getId());
        assertThat(opt).isEmpty();
    }

    @Test
    @WithMockCustomUser(id = "1", password = "secret-key", role = Role.USER, socialType = SocialType.KAKAO)
    void createReply() throws Exception {
        // given
        final long TEST_MEMBER_ID = 1L;
        Member member = memberRepository.save(EntityUtil.testMember(TEST_MEMBER_ID));
        Post post = postRepository.save(EntityUtil.testPost(member));
        CreateReplyDto createReplyDto = CreateReplyDto.builder()
                .postId(post.getId())
                .content("content")
                .build();

        String content = objectMapper.writeValueAsString(createReplyDto);

        // when
        ResultActions result = postRequest(mockMvc, "/reply", content);
        createDocument(result,"create-reply");
    }

    @Test
    @WithMockCustomUser(id = "1", password = "secret-key", role = Role.USER, socialType = SocialType.KAKAO)
    void createReReply() throws Exception {
        // given
        final long TEST_MEMBER_ID = 1L;
        Member member = memberRepository.save(EntityUtil.testMember(TEST_MEMBER_ID));
        Reply reply = replyRepository.save(EntityUtil.testReply(member));
        CreateReReplyDto createReReplyDto = CreateReReplyDto.builder()
                .replyId(reply.getId())
                .content("content")
                .build();

        String content = objectMapper.writeValueAsString(createReReplyDto);

        // when
        ResultActions result = postRequest(mockMvc, "/reply/re", content);
        createDocument(result, "create-reReply");
    }

    @Test
    @WithMockCustomUser(id = "1", password = "secret-key", role = Role.USER, socialType = SocialType.KAKAO)
    void deleteReply() throws Exception {
        // given
        final long TEST_MEMBER_ID = 1L;
        Member member = memberRepository.save(EntityUtil.testMember(TEST_MEMBER_ID));
        Reply reply = replyRepository.save(EntityUtil.testReply(member));

        // when
        ResultActions result = deleteRequest(mockMvc, "/reply/" + reply.getId());
        createDocument(result, "reply-delete-request");

        // then
        Optional<Reply> opt = replyRepository.findByIdAndAuthorId(reply.getId(), member.getId());
        assertThat(opt).isEmpty();
    }

    @Test
    @WithMockCustomUser(id = "1", password = "secret-key", role = Role.USER, socialType = SocialType.KAKAO)
    void deleteReReply() throws Exception {
        // given
        final long TEST_MEMBER_ID = 1L;
        Member member = memberRepository.save(EntityUtil.testMember(TEST_MEMBER_ID));
        ReReply reReply = reReplyRepository.save(EntityUtil.testReReply(member));

        // when
        ResultActions result = deleteRequest(mockMvc, "/reply/re/" + reReply.getId());
        createDocument(result, "reReply-delete-request");

        // then
        Optional<ReReply> opt = reReplyRepository.findByIdAndAuthorId(reReply.getId(), member.getId());
        assertThat(opt).isEmpty();
    }

    @Test
    @WithMockCustomUser(id = "1", password = "secret-key", role = Role.USER, socialType = SocialType.KAKAO)
    void updateReply() throws Exception {
        // given
        final long TEST_MEMBER_ID = 1L;
        Member member = memberRepository.save(EntityUtil.testMember(TEST_MEMBER_ID));
        Reply reply = replyRepository.save(EntityUtil.testReply(member));

        UpdateReplyDto updateReplyDto = new UpdateReplyDto("new-content");
        String content = objectMapper.writeValueAsString(updateReplyDto);

        // when
        ResultActions result = patchRequest(mockMvc, "/reply/" + reply.getId(), content);
        createDocument(result, "reply-update-request");

        // then
        reply = replyService.findReplyById(reply.getId());
        assertThat(reply.getContent()).isEqualTo("new-content");
    }

    @Test
    @WithMockCustomUser(id = "1", password = "secret-key", role = Role.USER, socialType = SocialType.KAKAO)
    void updateReReply() throws Exception {
        // given
        final long TEST_MEMBER_ID = 1L;
        Member member = memberRepository.save(EntityUtil.testMember(TEST_MEMBER_ID));
        ReReply reReply = reReplyRepository.save(EntityUtil.testReReply(member));

        UpdateReplyDto updateReplyDto = new UpdateReplyDto("new-content");
        String content = objectMapper.writeValueAsString(updateReplyDto);

        // when
        ResultActions result = patchRequest(mockMvc, "/reply/re/" + reReply.getId(), content);
        createDocument(result, "reReply-update-request");

        // then
        reReply = replyService.findReReplyById(reReply.getId());
        assertThat(reReply.getContent()).isEqualTo("new-content");
    }
}