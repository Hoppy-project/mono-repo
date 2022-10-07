package com.hoppy.app.meeting.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

import com.hoppy.app.meeting.Category;
import com.hoppy.app.meeting.domain.Meeting;
import com.hoppy.app.meeting.dto.PagingMeetingDto;
import com.hoppy.app.meeting.dto.ParticipantDto;
import com.hoppy.app.meeting.repository.MeetingRepository;
import com.hoppy.app.member.domain.Member;
import com.hoppy.app.member.domain.MemberMeeting;
import com.hoppy.app.member.service.MemberService;
import com.hoppy.app.response.error.exception.BusinessException;
import com.hoppy.app.response.error.exception.ErrorCode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.hoppy.app.utils.EntityUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 * @author 태경 2022-07-18
 */
@ExtendWith(MockitoExtension.class)
class MeetingServiceImplUnitTest {

    @InjectMocks
    MeetingServiceImpl meetingInquiryService;

    @Mock
    MemberService memberService;

    @Mock
    MeetingRepository meetingRepository;

    @DisplayName("ParticipantDtoList 반환 검증 테스트")
    @Test
    void getParticipantDtoListTest() {
        // given
        final long OWNER_ID = 5L;
        final int PARTICIPANT_COUNT = 10;

        List<Long> memberIdList = new ArrayList<>();
        List<Member> memberList = new ArrayList<>();

        Member owner = EntityUtil.testMember(OWNER_ID);
        Meeting meeting = Meeting.builder()
                .owner(owner)
                .title("test")
                .content("test")
                .memberLimit(15)
                .category(Category.HEALTH)
                .build();

        for (int i = 1; i <= PARTICIPANT_COUNT; i++) {
            Member member = EntityUtil.testMember(i);
            meeting.addParticipant(MemberMeeting.builder()
                    .member(member)
                    .meeting(meeting)
                    .build());
            memberList.add(member);
            memberIdList.add(member.getId());
        }
        given(memberService.infiniteScrollPagingMember(memberIdList, 0L,
                PageRequest.of(0, memberIdList.size())))
                .willReturn(memberList);

        // when
        List<ParticipantDto> participantList = meetingInquiryService.getParticipantDtoList(meeting);

        // then
        assertThat(participantList.size()).isEqualTo(memberList.size());
        assertThat(participantList.stream().filter(ParticipantDto::getOwner).count()).isEqualTo(1);
    }

    @DisplayName("모임 가입 요청 MAX_PARTICIPANTS 예외 발생 테스트")
    @Test
    void checkJoinRequestValidFailTest1() {
        //given
        Member owner = EntityUtil.testMember(1L);
        Meeting meeting = Meeting.builder()
                .owner(owner)
                .title("test")
                .content("test")
                .memberLimit(0)
                .category(Category.HEALTH)
                .fullFlag(true)
                .build();

        final var REQUEST_MEMBER_ID = 1111L;
        final var REQUEST_MEETING_ID = 2222L;

        given(meetingRepository.findByIdUsingLock(REQUEST_MEETING_ID)).willReturn(
                Optional.of(meeting));

        //when
        BusinessException exception = assertThrows(BusinessException.class,
                () -> meetingInquiryService.checkJoinRequestValid(REQUEST_MEETING_ID,
                        REQUEST_MEMBER_ID));

        //then
        assertEquals(ErrorCode.MAX_PARTICIPANTS.getMessage(), exception.getMessage());
    }

    @DisplayName("모임 가입 요청 ALREADY_JOIN 예외 발생 테스트")
    @Test
    void checkJoinRequestValidFailTest2() {
        //given
        Set<MemberMeeting> participants = new HashSet<>();
        final var REQUEST_MEMBER_ID = 1111L;
        final var REQUEST_MEETING_ID = 2222L;
        Member member = EntityUtil.testMember(REQUEST_MEMBER_ID);
        Meeting meeting = Meeting.builder()
                .owner(member)
                .title("test")
                .content("test")
                .memberLimit(5)
                .category(Category.HEALTH)
                .build();
        meeting.addParticipant(
                MemberMeeting.builder()
                        .member(member)
                        .meeting(meeting)
                        .build()
        );

        given(meetingRepository.findByIdUsingLock(REQUEST_MEETING_ID)).willReturn(
                Optional.of(meeting));

        //when
        BusinessException exception = assertThrows(BusinessException.class,
                () -> meetingInquiryService.checkJoinRequestValid(REQUEST_MEETING_ID, REQUEST_MEMBER_ID));

        //then
        assertEquals(ErrorCode.ALREADY_JOIN.getMessage(), exception.getMessage());
    }

    @DisplayName("모임 가입 요청 MEETING_NOT_FOUND 예외 발생 테스트")
    @Test
    void checkJoinRequestValidFailTest3() {
        //given
        final var REQUEST_MEMBER_ID = 1111L;
        final var REQUEST_MEETING_ID = 2222L;
        given(meetingRepository.findByIdUsingLock(REQUEST_MEETING_ID)).willReturn(
                Optional.empty());

        //when
        BusinessException exception = assertThrows(BusinessException.class,
                () -> meetingInquiryService.checkJoinRequestValid(REQUEST_MEETING_ID, REQUEST_MEMBER_ID));

        //then
        assertEquals(ErrorCode.MEETING_NOT_FOUND.getMessage(), exception.getMessage());
    }

    @DisplayName("모임 페이징 테스트: 더 이상 조회할 모임이 없을 경우")
    @Test
    void pagingMeetingExceptionTest() {
        //given
        final var REQ_CATEGORY_NUM = 2;
        final var REQ_LAST_ID = 0L;
        final var REQ_MEMBER_ID = 1L;
        Category category = Category.intToCategory(REQ_CATEGORY_NUM);
        Pageable pageable = PageRequest.of(0, 14);
        given(meetingRepository.infiniteScrollPaging(category, Long.MAX_VALUE, pageable)).willReturn(new ArrayList<>());

        // when
        PagingMeetingDto dto = meetingInquiryService.pagingMeeting(REQ_CATEGORY_NUM, REQ_LAST_ID, REQ_MEMBER_ID);

        // then
        assertThat(dto.getMeetingList()).isEmpty();
        assertThat(dto.getNextPagingUrl()).isEqualTo("end");
    }
}