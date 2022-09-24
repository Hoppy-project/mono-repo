package com.hoppy.app.story.controller;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoppy.app.like.repository.MemberStoryLikeRepository;
import com.hoppy.app.like.repository.MemberStoryReReplyLikeRepository;
import com.hoppy.app.like.repository.MemberStoryReplyLikeRepository;
import com.hoppy.app.login.WithMockCustomUser;
import com.hoppy.app.member.domain.Member;
import com.hoppy.app.member.repository.MemberRepository;
import com.hoppy.app.member.service.MemberService;
import com.hoppy.app.story.domain.story.Story;
import com.hoppy.app.story.domain.story.StoryReReply;
import com.hoppy.app.story.domain.story.StoryReply;
import com.hoppy.app.story.dto.StoryDetailDto;
import com.hoppy.app.story.dto.StoryReReplyRequestDto;
import com.hoppy.app.story.dto.StoryReplyRequestDto;
import com.hoppy.app.story.dto.UpdateStoryReReplyDto;
import com.hoppy.app.story.dto.UpdateStoryReplyDto;
import com.hoppy.app.story.repository.StoryReReplyRepository;
import com.hoppy.app.story.repository.StoryReplyRepository;
import com.hoppy.app.story.repository.StoryRepository;
import com.hoppy.app.story.service.StoryReplyService;
import com.hoppy.app.story.service.StoryService;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.print.attribute.standard.Media;
import org.assertj.core.api.Assertions;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
public class StoryLikeAndReplyControllerTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberService memberService;

    @Autowired
    StoryReplyRepository storyReplyRepository;

    @Autowired
    StoryReReplyRepository storyReReplyRepository;

    @Autowired
    StoryReplyService storyReplyService;

    @Autowired
    MemberStoryLikeRepository memberStoryLikeRepository;

    @Autowired
    MemberStoryReplyLikeRepository memberStoryReplyLikeRepository;

    @Autowired
    MemberStoryReReplyLikeRepository memberStoryReReplyLikeRepository;

    @Autowired
    StoryRepository storyRepository;

    @Autowired
    StoryService storyService;

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setup() {

        Member member1 = Member.builder().id(8669L).username("최대한").profileImageUrl("korea88@naver.com").build();
        Member member2 = Member.builder().id(7601L).username("김태경").profileImageUrl("seaworld@daum.net").build();

        memberRepository.save(member1);
        memberRepository.save(member2);

        for(int i = 1; i <= 5; i++) {
            Member member = null;
            if(i % 2 == 0) {
                member = member1;
            } else {
                member = member2;
            }
            Story story = storyRepository.save(
                    Story.builder()
                            .member(member)
                            .title(i+"th Story")
                            .content("This is " + i + "th Story")
                            .filePath(i+".jpg")
                            .member(member).build()
            );
            storyService.likeOrDislikeStory(member2.getId(), story.getId());
        }
    }

    @AfterEach
    void afterEach() {
        memberStoryReReplyLikeRepository.deleteAll();
        memberStoryReplyLikeRepository.deleteAll();
        storyReReplyRepository.deleteAll();
        storyReplyRepository.deleteAll();
        memberStoryLikeRepository.deleteAll();
        storyRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @DisplayName("스토리 좋아요 및 좋아요 취소 컨트롤러")
    @Test
    @WithMockCustomUser(id = "8669")
    void storyLikeTest() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<Story> storyList = storyRepository.findAll();
        Long storyId = storyList.get(0).getId();
        ResultActions result = mvc.perform(MockMvcRequestBuilders
                .get("/story/like")
                .param("id", String.valueOf(storyId))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
        );
        result.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("story-like-request",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @DisplayName("스토리 댓글 등록 컨트롤러")
    @Test
    @WithMockCustomUser(id = "8669")
    void uploadStoryReplyTest() throws Exception {
        List<Story> storyList = storyRepository.findAll();
        Long storyId = storyList.get(0).getId();
        StoryReplyRequestDto dto = StoryReplyRequestDto.builder().content("This is Story Reply Test").build();
        String content = objectMapper.writeValueAsString(dto);
        ResultActions result = mvc.perform(MockMvcRequestBuilders
                .post("/story/reply")
                .param("id", String.valueOf(storyId))
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
        );
        result.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("story-reply-upload",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @DisplayName("스토리 댓글 수정 컨트롤러")
    @Test
    @WithMockCustomUser(id = "8669")
    void updateStoryReplyTest() throws Exception {
        Story story = storyRepository.findAll().get(0);
        Member member = story.getMember();

        StoryReply reply = storyReplyRepository.save(
                StoryReply.builder().story(story).member(member).content("Test").build()
        );

        UpdateStoryReplyDto dto = UpdateStoryReplyDto.builder().content("Update").build();
        String content = objectMapper.writeValueAsString(dto);

        ResultActions result = mvc.perform(MockMvcRequestBuilders
                .put("/story/reply")
                .param("id", String.valueOf(reply.getId()))
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
        );

        result.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("story-reply-update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        reply = storyReplyService.findByReplyId(reply.getId());
        assertThat(reply.getContent()).isEqualTo("Update");
    }

    @DisplayName("스토리 댓글 삭제 컨트롤러")
    @Test
    @WithMockCustomUser(id = "8669")
    void deleteStoryReplyTest() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Long memberId = Long.parseLong(authentication.getName());

        List<Story> storyList = storyRepository.findAll();
        Long storyId = storyList.get(0).getId();

        for(int i = 0; i < 3; i++) {
            String content = "This is " + String.valueOf(i+1) + "th reply";
            StoryReplyRequestDto dto = StoryReplyRequestDto.builder()
                    .content(content)
                    .build();

            storyReplyService.uploadStoryReply(memberId, storyId, dto);
        }

        Long tmpStoryId = storyReplyRepository.findAll().get(0).getId();

        ResultActions result = mvc.perform(MockMvcRequestBuilders
                .delete("/story/reply")
                .param("id", String.valueOf(tmpStoryId))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
        );

        result.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("story-reply-delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @DisplayName("스토리 댓글 좋아요 컨트롤러")
    @Test
    @WithMockCustomUser(id = "8669")
    void likeOrDislikeStoryReplyTest() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long memberId = Long.parseLong(authentication.getName());

        List<Story> storyList = storyRepository.findAll();
        for(int i = 0; i < storyList.size(); i++) {
            storyReplyService.uploadStoryReply(memberId,
                    storyList.get(i).getId(), StoryReplyRequestDto.builder().content("Reply Like Test").build());
        }

        Long replyId = storyReplyRepository.findAll().get(0).getId();

        ResultActions result = mvc.perform(MockMvcRequestBuilders
                .get("/story/reply/like")
                .param("id", String.valueOf(replyId))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
        );

        result.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("story-reply-like-request",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @DisplayName("스토리 대댓글 업로드 컨트롤러")
    @Test
    @WithMockCustomUser(id = "8669")
    void uploadStoryReReplyTest() throws Exception {
        Long memberId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
        Long storyId = storyRepository.findAll().get(0).getId();

        StoryReplyRequestDto replyDto = StoryReplyRequestDto.builder().content("This is Story ReReply Test").build();
        Long replyId = storyReplyService.uploadStoryReply(memberId, storyId, replyDto).getId();

        StoryReReplyRequestDto reReplyDto = StoryReReplyRequestDto.builder().content("ReReply").build();
        String content = objectMapper.writeValueAsString(reReplyDto);

        ResultActions result = mvc.perform(MockMvcRequestBuilders
                .post("/story/reply/re")
                .param("id", String.valueOf(replyId))
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
        );
        result.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("story-reReply-upload",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @DisplayName("스토리 대댓글 수정 컨트롤러")
    @Test
    @WithMockCustomUser(id = "8669")
    void updateStoryReReplyTest() throws Exception {
        Story story = storyRepository.findAll().get(0);
        Member member = story.getMember();

        StoryReply reply = storyReplyRepository.save(
                StoryReply.builder().story(story).member(member).content("Test").build());

        StoryReReply reReply = storyReReplyRepository.save(
                StoryReReply.builder().reply(reply).content("Test").member(member).build());

        UpdateStoryReReplyDto dto = UpdateStoryReReplyDto.builder().content("Update").build();
        String content = objectMapper.writeValueAsString(dto);

        ResultActions result = mvc.perform(MockMvcRequestBuilders
                .put("/story/reply/re")
                .param("id", String.valueOf(reReply.getId()))
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
        );

        result.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("story-reReply-update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        reReply = storyReplyService.findByReReplyId(reReply.getId());
        assertThat(reReply.getContent()).isEqualTo("Update");
    }

    @DisplayName("스토리 대댓글 삭제 컨트롤러")
    @Test
    @WithMockCustomUser(id = "8669")
    void deleteStoryReReplyTest() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long memberId = Long.parseLong(authentication.getName());

        List<Story> storyList = storyRepository.findAll();
        for(int i = 0; i < storyList.size(); i++) {
            Long replyId = storyReplyService.uploadStoryReply(memberId, storyList.get(i).getId(),
                    StoryReplyRequestDto.builder().content("Reply Test").build()).getId();
            storyReplyService.uploadStoryReReply(memberId, replyId,
                    StoryReReplyRequestDto.builder().content("ReReply Test").build());
        }

        Long reReplyId = storyReReplyRepository.findAll().get(0).getId();

        ResultActions result = mvc.perform(MockMvcRequestBuilders
                .delete("/story/reply/re")
                .param("id", String.valueOf(reReplyId))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
        );

        result.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("story-reReply-delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @DisplayName("스토리 대댓글 좋아요 컨트롤러")
    @Test
    @WithMockCustomUser(id = "8669")
    void likeOrDislikeStoryReReplyTest() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long memberId = Long.parseLong(authentication.getName());

        List<Story> storyList = storyRepository.findAll();
        for (int i = 0; i < storyList.size(); i++) {
            Long replyId = storyReplyService.uploadStoryReply(memberId, storyList.get(i).getId(),
                    StoryReplyRequestDto.builder().content("Reply Test").build()).getId();
            storyReplyService.uploadStoryReReply(memberId, replyId,
                    StoryReReplyRequestDto.builder().content("ReReply Test").build());
        }

        Long reReplyId = storyReReplyRepository.findAll().get(0).getId();
        ResultActions result = mvc.perform(MockMvcRequestBuilders
                .get("/story/reply/re/like")
                .param("id", String.valueOf(reReplyId))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
        );
        result.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("story-reReply-like",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }
}
