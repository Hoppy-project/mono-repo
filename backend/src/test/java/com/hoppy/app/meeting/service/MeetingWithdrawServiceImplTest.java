package com.hoppy.app.meeting.service;

import com.hoppy.app.meeting.Category;
import com.hoppy.app.meeting.domain.Meeting;
import com.hoppy.app.meeting.repository.MeetingRepository;
import com.hoppy.app.member.domain.Member;
import com.hoppy.app.member.domain.MemberMeeting;
import com.hoppy.app.member.repository.MemberMeetingRepository;
import com.hoppy.app.member.repository.MemberRepository;
import com.hoppy.app.response.error.exception.BusinessException;
import com.hoppy.app.response.error.exception.ErrorCode;
import com.hoppy.app.utils.EntityUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * packageName    : com.hoppy.app.meeting.service
 * fileName       : MeetingServiceImplTest
 * author         : Kim
 * date           : 2022-08-30
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022-08-30        Kim        탈퇴 로직 통합 테스트 작성
 */
@SpringBootTest
@DisplayName("모임 탈퇴 서비스 통합 테스트")
class MeetingWithdrawServiceImplTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MeetingRepository meetingRepository;

    @Autowired
    MemberMeetingRepository memberMeetingRepository;

    @Autowired
    MeetingService meetingService;

    @AfterEach
    void clean() {
        memberMeetingRepository.deleteAll();
        meetingRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @DisplayName("모임 탈퇴 테스트")
    @Test
    void withdrawMeetingTest1() {
        // given
        Member owner = memberRepository.save(EntityUtil.testMember(99L));
        Member member = memberRepository.save(EntityUtil.testMember(1L));
        Meeting meeting = meetingRepository.save(
                Meeting.builder()
                        .owner(owner)
                        .url("test-url")
                        .title("test-title")
                        .content("test-content")
                        .category(Category.HEALTH)
                        .memberLimit(2)
                        .build()
        );
        memberMeetingRepository.save(MemberMeeting.of(owner, meeting));
        memberMeetingRepository.save(MemberMeeting.of(member, meeting));

        // when
        meetingService.withdrawMeeting(meeting.getId(), member.getId());

        // then
        List<MemberMeeting> memberMeetings = memberMeetingRepository.findALlByMeeting(meeting);
        assertThat(memberMeetings.size()).isEqualTo(1);
    }

    @DisplayName("모임 탈퇴 OWNER_WITHDRAW_ERROR 테스트")
    @Test
    void withdrawMeetingTest2() {
        // given
        Member owner = memberRepository.save(EntityUtil.testMember(99L));
        Member member = memberRepository.save(EntityUtil.testMember(1L));
        Meeting meeting = meetingRepository.save(
                Meeting.builder()
                        .owner(owner)
                        .url("test-url")
                        .title("test-title")
                        .content("test-content")
                        .category(Category.HEALTH)
                        .memberLimit(2)
                        .build()
        );
        memberMeetingRepository.save(MemberMeeting.of(owner, meeting));
        memberMeetingRepository.save(MemberMeeting.of(member, meeting));

        // when
        BusinessException ex = assertThrows(BusinessException.class, () -> meetingService.withdrawMeeting(meeting.getId(), owner.getId()));

        // then
        assertThat(ex.getMessage()).isEqualTo(ErrorCode.OWNER_WITHDRAW_ERROR.getMessage());
    }

    @DisplayName("모임 탈퇴 NOT_JOINED 테스트")
    @Test
    void withdrawMeetingTest3() {
        // given
        Member owner = memberRepository.save(EntityUtil.testMember(99L));
        Member member = memberRepository.save(EntityUtil.testMember(1L));
        Meeting meeting = meetingRepository.save(
                Meeting.builder()
                        .owner(owner)
                        .url("test-url")
                        .title("test-title")
                        .content("test-content")
                        .category(Category.HEALTH)
                        .memberLimit(2)
                        .build()
        );
        memberMeetingRepository.save(MemberMeeting.of(owner, meeting));

        // when
        BusinessException ex = assertThrows(BusinessException.class, () -> meetingService.withdrawMeeting(meeting.getId(), member.getId()));

        // then
        assertThat(ex.getMessage()).isEqualTo(ErrorCode.NOT_JOINED.getMessage());
    }
}