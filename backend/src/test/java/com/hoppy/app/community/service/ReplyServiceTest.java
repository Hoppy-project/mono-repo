package com.hoppy.app.community.service;

import com.hoppy.app.community.domain.Post;
import com.hoppy.app.community.domain.ReReply;
import com.hoppy.app.community.domain.Reply;
import com.hoppy.app.community.dto.CreateReReplyDto;
import com.hoppy.app.community.dto.CreateReplyDto;
import com.hoppy.app.community.dto.UpdateReplyDto;
import com.hoppy.app.community.repository.PostRepository;
import com.hoppy.app.community.repository.ReReplyRepository;
import com.hoppy.app.community.repository.ReplyRepository;
import com.hoppy.app.member.domain.Member;
import com.hoppy.app.member.repository.MemberRepository;
import com.hoppy.app.utils.EntityUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * packageName    : com.hoppy.app.community.service
 * fileName       : ReplyServiceTest
 * author         : Kim
 * date           : 2022-08-31
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022-08-31        Kim       최초 생성
 */
@SpringBootTest
class ReplyServiceTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ReplyRepository replyRepository;

    @Autowired
    ReReplyRepository reReplyRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    ReplyService replyService;

    @AfterEach
    void clean() {
        reReplyRepository.deleteAll();
        replyRepository.deleteAll();
        postRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    void createReply() {
        // given
        Member member = memberRepository.save(EntityUtil.testMember(1L));
        Post post = postRepository.save(EntityUtil.testPost(member));
        CreateReplyDto createReplyDto = CreateReplyDto.builder()
                .postId(post.getId())
                .content("content")
                .build();

        // when
        Reply reply = replyService.createReply(member.getId(), createReplyDto);

        // then
        assertThat(reply.getContent()).isEqualTo("content");
    }

    @Test
    void createReReply() {
        // given
        Member member = memberRepository.save(EntityUtil.testMember(1L));
        Reply reply = replyRepository.save(EntityUtil.testReply(member));
        CreateReReplyDto createReReplyDto = CreateReReplyDto.builder()
                .replyId(reply.getId())
                .content("content")
                .build();

        // when
        ReReply reReply = replyService.createReReply(member.getId(), createReReplyDto);

        // then
        assertThat(reReply.getContent()).isEqualTo("content");
    }

    @Test
    void deleteReply() {
        // given
        Member member = memberRepository.save(EntityUtil.testMember(1L));
        Reply reply = replyRepository.save(EntityUtil.testReply(member));

        for(int i = 0; i < 10; i++) {
            reReplyRepository.save(EntityUtil.testReReply(member, reply, "test"));
        }

        // when
        replyService.deleteReply(member.getId(), reply.getId());

        // then
        Optional<Reply> opt = replyRepository.findByIdAndAuthorId(reply.getId(), member.getId());
        assertThat(opt).isEmpty();
    }

    @Test
    void deleteReReply() {
        // given
        Member member = memberRepository.save(EntityUtil.testMember(1L));
        ReReply reReply = reReplyRepository.save(EntityUtil.testReReply(member));

        // when
        replyService.deleteReReply(member.getId(), reReply.getId());

        // then
        Optional<ReReply> opt = reReplyRepository.findByIdAndAuthorId(reReply.getId(), member.getId());
        assertThat(opt).isEmpty();
    }

    @Test
    void updateReplyTest() {
        // given
        Member member = memberRepository.save(EntityUtil.testMember(1L));
        Reply reply = replyRepository.save(EntityUtil.testReply(member));

        // when
        UpdateReplyDto updateReplyDto = new UpdateReplyDto("update");
        replyService.updateReply(updateReplyDto, 1L, reply.getId());

        // then
        reply = replyService.findReplyById(reply.getId());
        assertThat(reply.getContent()).isEqualTo("update");
    }

    @Test
    void updateReReplyTest() {
        // given
        Member member = memberRepository.save(EntityUtil.testMember(1L));
        ReReply reReply = reReplyRepository.save(EntityUtil.testReReply(member));

        // when
        UpdateReplyDto updateReplyDto = new UpdateReplyDto("update");
        replyService.updateReReply(updateReplyDto, 1L, reReply.getId());

        // then
        reReply = replyService.findReReplyById(reReply.getId());
        assertThat(reReply.getContent()).isEqualTo("update");
    }
}