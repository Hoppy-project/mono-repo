package com.hoppy.app.story.controller;

import com.hoppy.app.login.auth.authentication.CustomUserDetails;
import com.hoppy.app.member.domain.Member;
import com.hoppy.app.member.service.MemberService;
import com.hoppy.app.response.dto.ResponseDto;
import com.hoppy.app.response.service.ResponseService;
import com.hoppy.app.response.service.SuccessCode;
import com.hoppy.app.story.domain.story.Story;
import com.hoppy.app.story.domain.story.StoryReply;
import com.hoppy.app.story.dto.PagingStoryDto;
import com.hoppy.app.story.dto.SaveStoryDto;
import com.hoppy.app.story.dto.StoryDetailDto;
import com.hoppy.app.story.dto.StoryDto;
import com.hoppy.app.story.dto.StoryReReplyRequestDto;
import com.hoppy.app.story.dto.StoryReplyRequestDto;
import com.hoppy.app.story.dto.UpdateStoryReReplyDto;
import com.hoppy.app.story.dto.UpdateStoryReplyDto;
import com.hoppy.app.story.dto.UploadStoryDto;
import com.hoppy.app.story.service.StoryReplyService;
import com.hoppy.app.story.service.StoryService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/story")
public class StoryController {

    private final StoryService storyService;

    private final StoryReplyService storyReplyService;
    private final MemberService memberService;

    private final ResponseService responseService;


    @PostMapping
    public ResponseEntity<ResponseDto> uploadStory(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody @Valid UploadStoryDto dto) {
        Member member = memberService.findById(userDetails.getId());
        Story story = storyService.uploadStory(dto, member);
        storyService.saveStory(story, member);
        SaveStoryDto saveStoryDto = SaveStoryDto.of(story, member);
        return responseService.successResult(SuccessCode.UPLOAD_STORY_SUCCESS, saveStoryDto);
    }

    @PutMapping
    public ResponseEntity<ResponseDto> updateStory(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody @Valid UploadStoryDto dto, @RequestParam("id") String id) {
        Story story = storyService.updateStory(dto, Long.parseLong(id));
        Member member = memberService.findById(userDetails.getId());
        SaveStoryDto saveStoryDto = SaveStoryDto.of(story, member);
        return responseService.successResult(SuccessCode.UPDATE_STORY_SUCCESS, saveStoryDto);
    }

    @DeleteMapping
    public ResponseEntity<ResponseDto> deleteStory(@RequestParam("id") Long id) {
        storyService.deleteStory(id);
        return responseService.successResult(SuccessCode.DELETE_STORY_SUCCESS);
    }

    @GetMapping
    public ResponseEntity<ResponseDto> showStoryList(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam(value = "lastId", defaultValue = "0") Long lastId) {
        PagingStoryDto pagingStory = storyService.pagingStory(userDetails.getId(), lastId);
        return responseService.successResult(SuccessCode.INQUIRY_STORY_SUCCESS, pagingStory);
    }

    @GetMapping("/like")
    public ResponseEntity<ResponseDto> likeStory(@RequestParam(value = "id") Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        storyService.likeOrDislikeStory(userDetails.getId(), id);
        Story story = storyService.findByStoryId(id);
        return responseService.successResult(SuccessCode.INQUIRY_STORY_SUCCESS, StoryDto.of(story));
    }
    @GetMapping("/detail")
    public ResponseEntity<ResponseDto> showStoryDetails(@AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(value = "id") Long id) {
        StoryDetailDto storyDetailDto = storyService.showStoryDetails(userDetails.getId(), id);
        return responseService.successResult(SuccessCode.INQUIRY_STORY_SUCCESS, storyDetailDto);
    }

    @PostMapping("/reply")
    public ResponseEntity<ResponseDto> uploadReply(@RequestParam(value = "id") Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody StoryReplyRequestDto dto) {
        storyReplyService.uploadStoryReply(userDetails.getId(), id, dto);
        Story story = storyService.findByStoryId(id);
        return responseService.successResult(SuccessCode.INQUIRY_STORY_SUCCESS, StoryDetailDto.from(story));
    }

    @PutMapping("/reply")
    public ResponseEntity<ResponseDto> updateReply(@RequestParam(value = "id") Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody UpdateStoryReplyDto dto) {
        storyReplyService.updateStoryReply(userDetails.getId(), id, dto);
        return responseService.ok();
    }

    @DeleteMapping("/reply")
    public ResponseEntity<ResponseDto> deleteReply(
            @RequestParam(value = "id") Long replyId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        storyReplyService.deleteStoryReply(userDetails.getId(), replyId);
        return responseService.successResult(SuccessCode.DELETE_REPLY_SUCCESS);
    }


    @GetMapping("reply/like")
    public ResponseEntity<ResponseDto> likeStoryReply(
            @RequestParam(value = "id") Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        storyReplyService.likeOrDislikeStoryReply(userDetails.getId(), id);
        return responseService.ok();
    }

    @PostMapping("/reply/re")
    public ResponseEntity<ResponseDto> uploadReReply(@RequestParam(value = "id") Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody StoryReReplyRequestDto dto) {
        storyReplyService.uploadStoryReReply(userDetails.getId(), id, dto);
        return responseService.ok();
    }

    @PutMapping("/reply/re")
    public ResponseEntity<ResponseDto> updateReReply(@RequestParam(value = "id") Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody UpdateStoryReReplyDto dto) {
        storyReplyService.updateStoryReReply(userDetails.getId(), id, dto);
        return responseService.ok();
    }

    @DeleteMapping("/reply/re")
    public ResponseEntity<ResponseDto> deleteReReply(@RequestParam(value = "id") Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        storyReplyService.deleteStoryReReply(userDetails.getId(), id);
        return responseService.ok();
    }

    @GetMapping("/reply/re/like")
    public ResponseEntity<ResponseDto> likeStoryReReply(
            @RequestParam(value = "id") Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        storyReplyService.likeOrDisLikeStoryReReply(userDetails.getId(), id);
        return responseService.ok();
    }
}
