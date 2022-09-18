package com.hoppy.app.meeting.controller;

import com.hoppy.app.community.dto.PagingPostDto;
import com.hoppy.app.community.dto.PostDto;
import com.hoppy.app.community.service.PostService;
import com.hoppy.app.login.auth.authentication.CustomUserDetails;
import com.hoppy.app.meeting.domain.Meeting;
import com.hoppy.app.meeting.dto.*;
import com.hoppy.app.meeting.service.MeetingService;
import com.hoppy.app.member.domain.Member;
import com.hoppy.app.member.service.MemberService;
import com.hoppy.app.response.dto.ResponseDto;
import com.hoppy.app.response.service.ResponseService;
import com.hoppy.app.response.service.SuccessCode;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/meeting")
public class MeetingController {

    private final MeetingService meetingService;
    private final MemberService memberService;
    private final PostService postService;
    private final ResponseService responseService;

    @PostMapping
    public ResponseEntity<ResponseDto> createMeeting(
            @RequestBody CreateMeetingDto dto,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Member member = memberService.findById(userDetails.getId());
        Meeting meeting = meetingService.createMeeting(dto, member);
        meetingService.createAndSaveMemberMeetingData(meeting, member);
        return responseService.successResult(SuccessCode.CREATE_MEETING_SUCCESS);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseDto> patchMeeting(
            @PathVariable("id") long id,
            @RequestBody UpdateMeetingDto dto,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        meetingService.updateMeeting(dto, userDetails.getId(), id);
        return responseService.successResult(SuccessCode.UPDATE_MEETING_SUCCESS);
    }

    @PostMapping("/entry")
    public ResponseEntity<ResponseDto> joinMeeting(
            @RequestBody MeetingJoinDto meetingJoinDto,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long meetingId = meetingJoinDto.getMeetingId();
        meetingService.checkJoinRequestValid(meetingId, userDetails.getId());
        return responseService.successResult(SuccessCode.JOIN_MEETING_SUCCESS);
    }

    @DeleteMapping("/withdrawal")
    public ResponseEntity<ResponseDto> withdrawalMeeting(
            @RequestBody MeetingWithdrawalDto meetingWithdrawalDto,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        meetingService.withdrawMeeting(meetingWithdrawalDto.getMeetingId(), userDetails.getId());
        return responseService.successResult(SuccessCode.WITHDRAW_MEETING_SUCCESS);
    }

    @GetMapping
    public ResponseEntity<ResponseDto> getMeetingListByCategoryWithPaging(
            @RequestParam int categoryNumber,
            @RequestParam(value = "lastId", defaultValue = "0") long lastId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        PagingMeetingDto pagingMeetingDto = meetingService.pagingMeeting(categoryNumber, lastId, userDetails.getId());
        return responseService.successResult(SuccessCode.INQUIRY_MEETING_SUCCESS, pagingMeetingDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> getMeetingDetail(
            @PathVariable("id") long id,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Meeting meeting = meetingService.findById(id);
        List<ParticipantDto> participantList = meetingService.getParticipantDtoList(meeting);
        meetingService.checkJoinedMemberV2(participantList, userDetails.getId());

        boolean liked = memberService.checkMeetingLiked(userDetails.getId(), meeting.getId());
        MeetingDetailDto meetingDetailDto = MeetingDetailDto.of(meeting, participantList, liked);

        return responseService.successResult(SuccessCode.INQUIRE_MEETING_DETAIL_SUCCESS, meetingDetailDto);
    }

    @GetMapping("/like/{id}")
    public ResponseEntity<ResponseDto> likeMeeting(
            @PathVariable("id") long id,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        meetingService.likeMeeting(userDetails.getId(), id);
        return responseService.ok();
    }

    @DeleteMapping("/like/{id}")
    public ResponseEntity<ResponseDto> dislikeMeeting(
            @PathVariable("id") long id,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        meetingService.dislikeMeeting(userDetails.getId(), id);
        return responseService.ok();
    }

    @GetMapping("/posts")
    public ResponseEntity<ResponseDto> getPostsWithPaging(
            @RequestParam(value = "meetingId", defaultValue = "0") long meetingId,
            @RequestParam(value = "lastId", defaultValue = "0") long lastId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        lastId = postService.validCheckLastId(lastId);
        Meeting meeting = meetingService.findById(meetingId);
        List<Member> participantList = meetingService.getParticipantList(meeting);
        meetingService.checkJoinedMemberV1(participantList, userDetails.getId());

        List<PostDto> postDtoList = postService.pagingPostListV2(meeting, lastId, userDetails.getId());
        long lastPostId = postService.getLastId(postDtoList);
        String nextPagingUrl = postService.createNextPagingUrl(meetingId, lastPostId);
        PagingPostDto pagingPostDto = PagingPostDto.of(postDtoList, nextPagingUrl);

        return responseService.successResult(SuccessCode.INQUIRY_COMMUNITY_POSTS_SUCCESS, pagingPostDto);
    }
}
