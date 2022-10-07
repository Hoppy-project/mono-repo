package com.hoppy.app.community.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.hoppy.app.community.domain.Post;
import com.hoppy.app.community.domain.ReReply;
import com.hoppy.app.community.domain.Reply;
import com.hoppy.app.like.domain.MemberPostLike;
import com.hoppy.app.like.repository.MemberPostLikeRepository;
import com.hoppy.app.meeting.Category;
import com.hoppy.app.meeting.domain.Meeting;
import com.hoppy.app.meeting.repository.MeetingRepository;
import com.hoppy.app.member.domain.Member;
import com.hoppy.app.member.repository.MemberRepository;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;

import com.hoppy.app.utils.EntityUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

/**
 * @author 태경 2022-08-06
 */
@DataJpaTest
@Slf4j
// 외부 DB 사용할 때 주석 해제
//@AutoConfigureTestDatabase(replace = Replace.NONE)
//@ActiveProfiles("test")
class PostRepositoryTest {

    @Autowired
    PostRepository postRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MeetingRepository meetingRepository;

    @Autowired
    ReplyRepository replyRepository;

    @Autowired
    ReReplyRepository reReplyRepository;

    @Autowired
    MemberPostLikeRepository memberPostLikeRepository;

    @Autowired
    EntityManager em;

    @DisplayName("커뮤니티 게시물 페이지네이션 테스트")
    @Test
    void pagingPostListTest() {
        Member owner = memberRepository.save(
                Member.builder()
                        .id(1L)
                        .build()
        );
        // given
        Meeting meeting = meetingRepository.save(
                Meeting.builder()
                        .owner(owner)
                        .title("0-title")
                        .content("0-content")
                        .category(Category.LIFE)
                        .memberLimit(10)
                        .build()
        );

        final int POST_COUNT = 10;
        final int REPLY_COUNT = 20;

        for(int i = 1; i <= POST_COUNT; i++) {
            Member member = memberRepository.save(
                    Member.builder()
                            .id((long) i)
                            .username(i + "-name")
                            .build()
            );
            Post post = postRepository.save(
                    Post.builder()
                            .title(i + "-title")
                            .content(i + "-content")
                            .meeting(meeting)
                            .author(member)
                            .build()
            );
            for(int j = 0; j < REPLY_COUNT; j++) {
                replyRepository.save(
                    Reply.builder()
                            .post(post)
                            .author(member)
                            .content(j + "-content")
                            .build()
                );
            }
        }
        em.flush();
        em.clear();

        // when
        final int PAGING_SIZE = 8;
        List<Post> posts = postRepository.infiniteScrollPagingPost(meeting, Long.MAX_VALUE, PageRequest.of(0, PAGING_SIZE));

        for (var p : posts) {
            var o = p.getAuthor();
            var replies = p.getReplies();

            int sum = 0;
            for(var r : replies) {
                sum += r.getReReplies().size();
            }

            System.out.println(
                    "[" + o.getId() + "]: "
                    + o.getUsername()
                    + " >> replies: "
                    + replies.size()
                    + " >> reReplies: "
                    + sum
            );
        }

        // then
        assertThat(posts.size()).isEqualTo(PAGING_SIZE);
    }

    @DisplayName("게시물 상세 조회 쿼리 테스트")
    @Test
    void getPostDetailTest() {
        // given
        long MEMBER_ID_COUNT = 1L;
        Member postAuthor = memberRepository.save(
                Member.builder()
                        .id(MEMBER_ID_COUNT++)
                        .build()
        );
        Post post = postRepository.save(
                Post.builder()
                        .title("test-title")
                        .content("test-content")
                        .author(postAuthor)
                        .build()
        );

        final int REPLY_COUNT = 20;
        final int RE_REPLY_COUNT = 5;

        for(int i = 0; i < REPLY_COUNT; i++) {
            Member replyAuthor = memberRepository.save(
                    Member.builder()
                            .id(MEMBER_ID_COUNT++)
                            .build()
            );
            Reply reply = replyRepository.save(
                    Reply.builder()
                            .author(replyAuthor)
                            .content(i + "-content")
                            .post(post)
                            .build()
            );

            for(int j = 0; j < RE_REPLY_COUNT; j++) {
                Member reReplyAuthor = memberRepository.save(
                        Member.builder()
                                .id(MEMBER_ID_COUNT++)
                                .build()
                );
                ReReply reReply = reReplyRepository.save(
                        ReReply.builder()
                                .author(reReplyAuthor)
                                .content(i + "-content")
                                .reply(reply)
                                .build()
                );
            }
        }

        em.flush();
        em.clear();

        // when
        Optional<Post> optionalPost = postRepository.getPostDetail(post.getId());
        assertThat(optionalPost).isPresent();
        post = optionalPost.get();

        // then
        assertThat(post.getReplies().size()).isEqualTo(REPLY_COUNT);
        post.getReplies().forEach(R -> assertThat(R.getReReplies().size()).isEqualTo(RE_REPLY_COUNT));
    }

    @DisplayName("게시물 좋아요 테스트")
    @Test
    void likeTest() {
        // given
        final long TEST_MEMBER_ID = 1L;
        Member member = memberRepository.save(
            Member.builder()
                .id(TEST_MEMBER_ID)
                .build()
        );

        Post post = postRepository.save(EntityUtil.testPost(member));
        memberPostLikeRepository.save(MemberPostLike.of(member, post));
        em.flush();
        em.clear();

        // when
        Optional<MemberPostLike> opt = memberPostLikeRepository.findByMemberIdAndPostId(member.getId(), post.getId());

        // then
        assertThat(opt).isPresent();
    }

    @DisplayName("게시물 좋아요 취소 테스트")
    @Test
    void dislikeTest() {
        // given
        final long TEST_MEMBER_ID = 1L;
        Member member = memberRepository.save(
                Member.builder()
                        .id(TEST_MEMBER_ID)
                        .build()
        );
        Post post = postRepository.save(EntityUtil.testPost(member));
        memberPostLikeRepository.save(MemberPostLike.of(member, post));
        memberPostLikeRepository.deleteByMemberIdAndPostId(member.getId(), post.getId());
        em.flush();
        em.clear();

        // when
        Optional<MemberPostLike> opt = memberPostLikeRepository.findByMemberIdAndPostId(member.getId(), post.getId());

        // then
        assertThat(opt).isEmpty();
    }
}