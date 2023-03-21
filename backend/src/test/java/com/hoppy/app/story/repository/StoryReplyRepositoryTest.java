package com.hoppy.app.story.repository;

import static org.assertj.core.api.Assertions.*;

import com.hoppy.app.like.domain.MemberStoryReReplyLike;
import com.hoppy.app.like.repository.MemberStoryReReplyLikeRepository;
import com.hoppy.app.member.domain.Member;
import com.hoppy.app.member.repository.MemberRepository;
import com.hoppy.app.story.domain.story.Story;
import com.hoppy.app.story.domain.story.StoryReReply;
import com.hoppy.app.story.domain.story.StoryReply;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@TestInstance(Lifecycle.PER_CLASS)
class StoryReplyRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    StoryRepository storyRepository;
    
    @Autowired
    StoryReplyRepository replyRepository;
    
    @Autowired
    StoryReReplyRepository reReplyRepository;

    @Autowired
    MemberStoryReReplyLikeRepository reReplyLikeRepository;

    @Autowired
    EntityManager em;

    @AfterEach
    void after() {
        reReplyRepository.deleteAll();
        replyRepository.deleteAll();
        storyRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    void deleteStoryReplyByStoryId() {
    }

    @Test
    void deleteStoryReReplyByReplyId() {
        
        Member author = memberRepository.save(Member.builder().id(1L).username("author").build());
        Member user = memberRepository.save(Member.builder().id(2L).username("user").build());

        Story story = Story.builder().title("story").member(author).content("test").build();
        storyRepository.save(story);
        
        StoryReply reply = StoryReply.builder().member(user).story(story).content("reply").build();
        replyRepository.save(reply);
        
        StoryReReply reReply = StoryReReply.builder().reply(reply).member(author).content("reReply").build();
        reReplyRepository.save(reReply);

        reReplyLikeRepository.save(MemberStoryReReplyLike.of(user, reReply));
        reReplyLikeRepository.save(MemberStoryReReplyLike.of(author, reReply));
        assertThat(reReplyLikeRepository.findAll().size()).isEqualTo(2);

        reReplyLikeRepository.deleteReReplyLikes(reply.getId());

        replyRepository.deleteReReplies(reply.getId());

        assertThat(reReplyRepository.findAll().size()).isEqualTo(0);
    }
}