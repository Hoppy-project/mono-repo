package com.hoppy.app.community.repository;

import com.hoppy.app.community.domain.ReReply;
import com.hoppy.app.community.domain.Reply;
import com.hoppy.app.like.domain.MemberReReplyLike;
import com.hoppy.app.like.domain.MemberReplyLike;
import com.hoppy.app.like.repository.MemberReReplyLikeRepository;
import com.hoppy.app.like.repository.MemberReplyLikeRepository;
import com.hoppy.app.member.domain.Member;
import com.hoppy.app.member.repository.MemberRepository;
import com.hoppy.app.utils.EntityUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * packageName    : com.hoppy.app.community.repository
 * fileName       : ReplyRepositoryTest
 * author         : Kim
 * date           : 2022-08-28
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022-08-28        Kim       최초 생성
 */
@DataJpaTest
@Slf4j
public class ReplyRepositoryTest {


    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ReplyRepository replyRepository;

    @Autowired
    ReReplyRepository reReplyRepository;

    @Autowired
    MemberReplyLikeRepository memberReplyLikeRepository;

    @Autowired
    MemberReReplyLikeRepository memberReReplyLikeRepository;

    @Autowired
    EntityManager em;

    @DisplayName("댓글 좋아요 테스트")
    @Test
    @Order(1)
    void replyLikeTest() {
        // given
        final long TEST_MEMBER_ID = 1L;
        Member member = memberRepository.save(EntityUtil.testMember(TEST_MEMBER_ID));
        Reply reply = replyRepository.save(EntityUtil.testReply(member));
        memberReplyLikeRepository.save(MemberReplyLike.of(member, reply));
        em.flush();
        em.clear();

        // when
        Optional<MemberReplyLike> opt = memberReplyLikeRepository.findByMemberIdAndReplyId(member.getId(), reply.getId());

        // then
        assertThat(opt).isPresent();
    }

    @DisplayName("댓글 좋아요 취소 테스트")
    @Test
    void replyDislikeTest() {
        // given
        final long TEST_MEMBER_ID = 1L;
        Member member = memberRepository.save(EntityUtil.testMember(TEST_MEMBER_ID));
        Reply reply = replyRepository.save(EntityUtil.testReply(member));
        memberReplyLikeRepository.save(MemberReplyLike.of(member, reply));
        em.flush();
        em.clear();

        // when
        memberReplyLikeRepository.delete(
                memberReplyLikeRepository.findByMemberIdAndReplyId(member.getId(), reply.getId())
                        .orElseThrow()
        );
        em.flush();
        em.clear();

        // then
        assertThat(
                memberReplyLikeRepository.findByMemberIdAndReplyId(member.getId(), reply.getId())
        ).isEmpty();
    }

    @DisplayName("대댓글 좋아요 테스트")
    @Test
    void reReplyLikeTest() {
        // given
        final long TEST_MEMBER_ID = 1L;
        Member member = memberRepository.save(EntityUtil.testMember(TEST_MEMBER_ID));
        ReReply reReply = reReplyRepository.save(EntityUtil.testReReply(member));
        memberReReplyLikeRepository.save(MemberReReplyLike.of(member, reReply));
        em.flush();
        em.clear();

        // when
        Optional<MemberReReplyLike> opt = memberReReplyLikeRepository.findByMemberIdAndReplyId(member.getId(), reReply.getId());

        // then
        assertThat(opt).isPresent();
    }

    @DisplayName("대댓글 좋아요 취소 테스트")
    @Test
    void reReplyDislikeTest() {
        // given
        final long TEST_MEMBER_ID = 1L;
        Member member = memberRepository.save(EntityUtil.testMember(TEST_MEMBER_ID));
        ReReply reReply = reReplyRepository.save(EntityUtil.testReReply(member));
        memberReReplyLikeRepository.save(MemberReReplyLike.of(member, reReply));
        em.flush();
        em.clear();

        // when
        memberReReplyLikeRepository.delete(
                memberReReplyLikeRepository.findByMemberIdAndReplyId(member.getId(), reReply.getId())
                        .orElseThrow()
        );
        em.flush();
        em.clear();

        // then
        assertThat(
                memberReReplyLikeRepository.findByMemberIdAndReplyId(member.getId(), reReply.getId())
        ).isEmpty();
    }
}
