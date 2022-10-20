package com.hoppy.app.member.controller;

import com.hoppy.app.like.domain.MemberMeetingLike;
import com.hoppy.app.like.repository.MemberMeetingLikeRepository;
import com.hoppy.app.login.auth.authentication.CustomUserDetails;
import com.hoppy.app.member.domain.Member;
import com.hoppy.app.member.domain.MemberMeeting;
import com.hoppy.app.member.dto.LikeMeetingsDto;
import com.hoppy.app.member.dto.MyProfileDto;
import com.hoppy.app.member.dto.UpdateMemberDto;
import com.hoppy.app.member.dto.UserProfileDto;
import com.hoppy.app.member.repository.MemberMeetingRepository;
import com.hoppy.app.member.service.MemberService;
import com.hoppy.app.response.dto.ResponseDto;
import com.hoppy.app.response.error.exception.BusinessException;
import com.hoppy.app.response.error.exception.ErrorCode;
import com.hoppy.app.response.service.ResponseService;
import com.hoppy.app.response.service.SuccessCode;
import com.hoppy.app.story.dto.SaveStoryDto;
import com.hoppy.app.story.service.StoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profile")
public class MemberProfileController {

    private final ResponseService responseService;
    private final StoryService storyService;
    private final MemberService memberService;

    private final MemberMeetingLikeRepository memberMeetingLikeRepository;

    private final MemberMeetingRepository memberMeetingRepository;

    @GetMapping
    public ResponseEntity<ResponseDto> showMyProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {
        MyProfileDto myProfileDto = memberService.getMyProfileInfo(userDetails.getId());
        return responseService.successResult(SuccessCode.SHOW_PROFILE_SUCCESS, myProfileDto);
    }

    @GetMapping("/story")
    private ResponseEntity<ResponseDto> showMyStories(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long memberId = userDetails.getId();
        Member member = memberService.findById(memberId);
        List<SaveStoryDto> storyDetails = storyService.showMyStoriesInProfile(member);
        return responseService.successResult(SuccessCode.SHOW_PROFILE_SUCCESS, storyDetails);
    }

    @GetMapping("/member")
    public ResponseEntity<ResponseDto> showUserProfile(@RequestParam("id") String id) {
        Long memberId = Long.parseLong(id);
        Member member = memberService.findById(memberId);
        if(member.isDeleted()) {
            throw new BusinessException(ErrorCode.DELETED_MEMBER);
        }
        UserProfileDto userProfileDto = UserProfileDto.of(member);
        return responseService.successResult(SuccessCode.SHOW_PROFILE_SUCCESS, userProfileDto);
    }

    @PutMapping
    public ResponseEntity<ResponseDto> updateUser(
            @AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody UpdateMemberDto memberDto) {
        Member member = memberService.updateById(userDetails.getId(), memberDto);
        UpdateMemberDto updateMemberDto = UpdateMemberDto.of(member);
        return responseService.successResult(SuccessCode.UPDATE_SUCCESS, updateMemberDto);
    }
}
