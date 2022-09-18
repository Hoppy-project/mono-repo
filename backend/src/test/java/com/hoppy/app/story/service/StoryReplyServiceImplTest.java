package com.hoppy.app.story.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoppy.app.like.repository.MemberStoryLikeRepository;
import com.hoppy.app.login.WithMockCustomUser;
import com.hoppy.app.member.domain.Member;
import com.hoppy.app.member.repository.MemberRepository;
import com.hoppy.app.member.service.MemberService;
import com.hoppy.app.story.domain.story.Story;
import com.hoppy.app.story.domain.story.StoryReReply;
import com.hoppy.app.story.domain.story.StoryReply;
import com.hoppy.app.story.dto.StoryReReplyRequestDto;
import com.hoppy.app.story.dto.StoryReplyRequestDto;
import com.hoppy.app.story.dto.UpdateStoryReReplyDto;
import com.hoppy.app.story.dto.UpdateStoryReplyDto;
import com.hoppy.app.story.repository.StoryReReplyRepository;
import com.hoppy.app.story.repository.StoryReplyRepository;
import com.hoppy.app.story.repository.StoryRepository;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
class StoryReplyServiceImplTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberService memberService;

    @Autowired
    MemberStoryLikeRepository memberStoryLikeRepository;

    @Autowired
    StoryRepository storyRepository;

    @Autowired
    StoryService storyService;
    @Autowired
    StoryReplyService storyReplyService;

    @Autowired
    StoryReplyRepository storyReplyRepository;

    @Autowired
    StoryReReplyRepository storyReReplyRepository;
    
    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @AfterEach
    void clean() {
        memberStoryLikeRepository.deleteAll();
        storyReReplyRepository.deleteAll();
        storyReplyRepository.deleteAll();
        storyRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("스토리 댓글 수정 테스트")
    @WithMockCustomUser(id = "8669")
    void updateStoryReply() {
        Member member = memberRepository.save(
                Member.builder().id(8669L).profileImageUrl("test.com").build()
        );
        Story story = storyRepository.save(
                Story.builder().content("this is test story").title("first").member(member).build()
        );
        storyReplyRepository.save(
                StoryReply.builder().story(story).member(member).content("Before").build()
        );
        Long replyId = storyReplyRepository.findAll().get(0).getId();
        UpdateStoryReplyDto dto = UpdateStoryReplyDto.builder().content("After").build();
        storyReplyService.updateStoryReply(8669L, replyId, dto);
        StoryReply tmpReply = storyReplyRepository.findById(replyId).get();
        assertThat(tmpReply.getContent()).isEqualTo("After");
    }

    @Test
    @DisplayName("스토리 대댓글 수정 테스트")
    @WithMockCustomUser(id = "8669")
    void updateStoryReReply() {
        Member member = memberRepository.save(
                Member.builder().id(8669L).profileImageUrl("test.com").build()
        );
        Story story = storyRepository.save(
                Story.builder().content("this is test story").title("first").member(member).build()
        );
        StoryReply reply = storyReplyRepository.save(
                StoryReply.builder().story(story).member(member).content("Before").build()
        );
        storyReReplyRepository.save(
                StoryReReply.builder().reply(reply).content("Before").member(member).build()
        );
        Long reReplyId = storyReReplyRepository.findAll().get(0).getId();
        UpdateStoryReReplyDto dto = UpdateStoryReReplyDto.builder().content("After").build();
        storyReplyService.updateStoryReReply(8669L, reReplyId, dto);
        StoryReReply tmpReReply = storyReReplyRepository.findById(reReplyId).get();
        assertThat(tmpReReply.getContent()).isEqualTo("After");
    }

    @Test
    @DisplayName("스토리 대댓글 업로드 테스트")
    void uploadStoryReReply() {
        Member member = memberRepository.save(
                Member.builder().id(8669L).profileImageUrl("test.com").build()
        );
        Story story = storyRepository.save(
                Story.builder().content("this is test story").title("first").member(member).build()
        );
        StoryReplyRequestDto storyReplyRequestDto =
                StoryReplyRequestDto.builder().content("reply").build();

        Story tmpStory = storyRepository.findAll().get(0);
        Long storyId = tmpStory.getId();
        System.out.println("storyId = " + storyId);
        StoryReply reply = storyReplyService.uploadStoryReply(member.getId(), storyId, storyReplyRequestDto);

        assertThat(reply.getReReplies().size()).isEqualTo(0);

        StoryReReplyRequestDto dto = StoryReReplyRequestDto.builder().content("ReReply").build();
        StoryReReply reReply = storyReplyService.uploadStoryReReply(member.getId(), 1L, dto);

        assertThat(reReply.getContent()).isEqualTo("ReReply");
    }
}