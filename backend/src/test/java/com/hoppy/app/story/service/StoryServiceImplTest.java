package com.hoppy.app.story.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoppy.app.like.domain.MemberStoryLike;
import com.hoppy.app.like.repository.MemberStoryLikeRepository;
import com.hoppy.app.login.WithMockCustomUser;
import com.hoppy.app.member.domain.Member;
import com.hoppy.app.member.repository.MemberRepository;
import com.hoppy.app.member.service.MemberService;
import com.hoppy.app.response.error.exception.BusinessException;
import com.hoppy.app.response.error.exception.ErrorCode;
import com.hoppy.app.story.domain.story.Story;
import com.hoppy.app.story.domain.story.StoryReReply;
import com.hoppy.app.story.domain.story.StoryReply;
import com.hoppy.app.story.dto.PagingStoryDto;
import com.hoppy.app.story.dto.StoryDetailDto;
import com.hoppy.app.story.dto.StoryReReplyRequestDto;
import com.hoppy.app.story.dto.StoryReplyRequestDto;
import com.hoppy.app.story.dto.UploadStoryDto;
import com.hoppy.app.story.repository.StoryReReplyRepository;
import com.hoppy.app.story.repository.StoryReplyRepository;
import com.hoppy.app.story.repository.StoryRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
class StoryServiceImplTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberService memberService;
    
    @Autowired
    MemberStoryLikeRepository memberStoryLikeRepository;

    @Autowired
    StoryRepository storyRepository;

    @Autowired
    StoryReplyRepository storyReplyRepository;

    @Autowired
    StoryReReplyRepository storyReReplyRepository;

    @Autowired
    StoryService storyService;

    @Autowired
    StoryReplyService storyReplyService;

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    EntityManager em;

    @AfterEach
    void clean() {
        memberStoryLikeRepository.deleteAll();
        storyReReplyRepository.deleteAll();
        storyReplyRepository.deleteAll();
        storyRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @DisplayName("스토리 좋아요 기능 테스트")
    @Test
    void storyLikeTest() {
        Member member = memberRepository.save(Member.builder()
                .id(8669L)
                .profileImageUrl("www")
                .build()
        );
        Story story = storyRepository.save(Story.builder()
                .title("Story Like Test")
                .content("This is Test")
                .member(member)
                .build()
        );

        storyService.likeOrDislikeStory(member.getId(), story.getId());

        assertThat(memberStoryLikeRepository.findAll().size()).isEqualTo(1);
    }

    @DisplayName("스토리 삭제 테스트")
    @Test
    void deleteStory() {
        Member member = memberRepository.save(Member.builder()
                .id(8669L)
                .build()
        );
        Story story = storyRepository.save(Story.builder()
                .title("Story Like Test")
                .content("This is Test")
                .member(member)
                .build()
        );
        Long storyId = story.getId();
        StoryReply reply = storyReplyRepository.save(
                StoryReply.builder().story(story).member(member).content("Reply").build()
        );
        StoryReReply reReply = storyReReplyRepository.save(
                StoryReReply.builder().reply(reply).member(member).content("ReReply").build()
        );
        storyService.likeOrDislikeStory(member.getId(), storyId);
        storyReplyService.likeOrDislikeStoryReply(member.getId(), reply.getId());
        storyReplyService.likeOrDisLikeStoryReReply(member.getId(), reReply.getId());
        storyService.deleteStory(storyId);
        assertThat(storyReReplyRepository.findAll()).isEmpty();
        assertThat(storyReplyRepository.findAll()).isEmpty();
        assertThat(storyRepository.findAll()).isEmpty();
    }
}