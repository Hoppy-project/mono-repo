package com.hoppy.app.member.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.hoppy.app.meeting.domain.Meeting;
import com.hoppy.app.meeting.repository.MeetingRepository;
import com.hoppy.app.member.domain.Member;
import com.hoppy.app.member.domain.MemberMeeting;
import java.util.Optional;

import com.hoppy.app.utils.EntityUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;

/**
 * @author 태경 2022-07-29
 */
@DataJpaTest
@TestInstance(Lifecycle.PER_METHOD)
class MemberMeetingRepositoryTest {

    @Autowired
    MemberMeetingRepository memberMeetingRepository;

    @Autowired
    MeetingRepository meetingRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager em;

    @BeforeEach
    void before() {
        memberMeetingRepository.deleteAll();
        meetingRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    void deleteMemberMeetingByMeetingIdAndMemberId() {

        //given
        Member member = memberRepository.save(EntityUtil.testMember(1L));
        Meeting meeting = meetingRepository.save(EntityUtil.testArtMeeting(member));
        memberMeetingRepository.save(
                MemberMeeting.builder()
                        .member(member)
                        .meeting(meeting)
                        .build()
        );
        em.flush();
        em.clear();

        //when
        memberMeetingRepository.deleteMemberMeetingByMeetingAndMember(meeting, member);

        //then
        Optional<Meeting> meetingOptional = meetingRepository.findById(meeting.getId());
        assert meetingOptional.isPresent();

        meeting = meetingOptional.get();
        assertThat(meeting.getParticipants().size()).isEqualTo(0);
    }
}