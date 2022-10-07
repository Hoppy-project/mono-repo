package com.hoppy.app.member.service;

import com.hoppy.app.like.domain.MemberMeetingLike;
import com.hoppy.app.like.repository.MemberMeetingLikeRepository;
import com.hoppy.app.meeting.domain.Meeting;
import com.hoppy.app.meeting.repository.MeetingRepository;
import com.hoppy.app.meeting.service.MeetingService;
import com.hoppy.app.member.domain.Member;
import com.hoppy.app.member.domain.MemberMeeting;
import com.hoppy.app.member.dto.MyProfileDto;
import com.hoppy.app.member.repository.MemberMeetingRepository;
import com.hoppy.app.member.repository.MemberRepository;
import com.hoppy.app.utility.EntityUtility;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class MemberMeetingLikeTest {

    @Autowired
    MemberMeetingRepository memberMeetingRepository;
    
    @Autowired
    MemberMeetingLikeRepository memberMeetingLikeRepository;
    
    @Autowired
    MemberRepository memberRepository;
    
    @Autowired
    MeetingRepository meetingRepository;

    @Autowired
    MeetingService meetingService;
    
    @Test
    @DisplayName("멤버 프로필 관심 모임 테스트")
    void memberMeetingLikeTest() {
        Member member = memberRepository.save(EntityUtility.testMember(1L));
        Meeting meeting = meetingRepository.save(EntityUtility.testArtMeeting(member));
        memberMeetingRepository.save(
                MemberMeeting.builder()
                        .member(member)
                        .meeting(meeting)
                        .build()
        );

        meetingService.likeMeeting(member.getId(), meeting.getId());

        Optional<MemberMeetingLike> meetingLike = memberMeetingLikeRepository.findByMemberIdAndMeetingId(member.getId(), meeting.getId());
        if(meetingLike.isPresent()) {
            System.out.println("@@@@@@@@@@@@@@@@@@");
            System.out.println("meetingLike.get().getMeetingId() = " + meetingLike.get().getMeetingId());
        }

        System.out.println("member.getMeetingLikes().size() = " + member.getMeetingLikes().size());
        System.out.println("member.getMeetingLikes() = " + member.getMeetingLikes());
        
    }
}
