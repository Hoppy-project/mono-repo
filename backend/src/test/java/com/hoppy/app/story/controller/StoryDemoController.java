package com.hoppy.app.story.controller;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoppy.app.login.WithMockCustomUser;
import com.hoppy.app.member.domain.Member;
import com.hoppy.app.member.repository.MemberRepository;
import com.hoppy.app.member.service.MemberService;
import com.hoppy.app.story.domain.story.Story;
import com.hoppy.app.story.dto.PagingStoryDto;
import com.hoppy.app.story.repository.StoryRepository;
import com.hoppy.app.story.service.StoryService;
import java.nio.charset.StandardCharsets;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
public class StoryDemoController {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberService memberService;

    @Autowired
    StoryRepository storyRepository;

    @Autowired
    StoryService storyService;

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @DisplayName("스토리 페이징 오류 테스트")
    @Test
    @WithMockCustomUser(id = "2")
    void storyPagingErrorTest() throws Exception {
        Member member1 = memberRepository.save(
                Member.builder().id(1L).username("first").build()
        );
        Member member2 = memberRepository.save(
                Member.builder().id(2L).username("second").build()
        );
        Story story1 = storyRepository.save(
                Story.builder().member(member1).title("1st").content("test").build()
        );
        Story story2 = storyRepository.save(
                Story.builder().member(member2).title("2st").content("test").build()
        );
        storyService.likeOrDislikeStory(member1.getId(), story1.getId());
        PagingStoryDto dto = storyService.pagingStory(member1.getId(), 0L);

        assertThat(dto.getStoryList().size()).isEqualTo(2);

        ResultActions result = mvc.perform(MockMvcRequestBuilders
                .get("/story?lastId=1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
        ).andDo(print());
    }
}
