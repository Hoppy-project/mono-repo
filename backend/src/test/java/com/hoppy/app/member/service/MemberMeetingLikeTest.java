package com.hoppy.app.member.service;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.core.Is.is;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.hoppy.app.like.domain.MemberMeetingLike;
import com.hoppy.app.like.repository.MemberMeetingLikeRepository;
import com.hoppy.app.login.WithMockCustomUser;
import com.hoppy.app.login.auth.SocialType;
import com.hoppy.app.login.auth.authentication.CustomUserDetails;
import com.hoppy.app.meeting.domain.Meeting;
import com.hoppy.app.meeting.repository.MeetingRepository;
import com.hoppy.app.meeting.service.MeetingService;
import com.hoppy.app.member.Role;
import com.hoppy.app.member.domain.Member;
import com.hoppy.app.member.domain.MemberMeeting;
import com.hoppy.app.member.dto.MyProfileDto;
import com.hoppy.app.member.repository.MemberMeetingRepository;
import com.hoppy.app.member.repository.MemberRepository;
import com.hoppy.app.utils.EntityUtil;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
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

    @AfterEach
    void afterEach() {
        memberMeetingLikeRepository.deleteAll();
        memberMeetingRepository.deleteAll();
        meetingRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("멤버 프로필 관심 모임 테스트")
    void memberMeetingLikeTest() {
        Member member = memberRepository.save(EntityUtil.testMember(1L));
        Meeting meeting1 = meetingRepository.save(EntityUtil.testArtMeeting(member));
        Meeting meeting2 = meetingRepository.save(EntityUtil.testHealthMeeting(member));

        memberMeetingRepository.save(MemberMeeting.builder().meeting(meeting1).member(member).build());

        meetingService.likeMeeting(member.getId(), meeting1.getId());
        meetingService.likeMeeting(member.getId(), meeting2.getId());

        List<MemberMeeting> meetingList = memberMeetingRepository.findAllByMember(member);

        List<MemberMeetingLike> meetingLikeList = memberMeetingLikeRepository.findAllByMember(member);

        assertThat(meetingLikeList.size()).isEqualTo(2);

        MyProfileDto dto = MyProfileDto.of(member, meetingList, meetingLikeList);

        assertThat(dto.getLikeMeetingsId().size()).isEqualTo(2);
        assertThat(dto.getMyMeetingsId().size()).isEqualTo(1);
    }
}
