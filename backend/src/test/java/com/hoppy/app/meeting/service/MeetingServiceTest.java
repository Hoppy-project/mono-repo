package com.hoppy.app.meeting.service;

import com.hoppy.app.meeting.Category;
import com.hoppy.app.meeting.domain.Meeting;
import com.hoppy.app.meeting.dto.UpdateMeetingDto;
import com.hoppy.app.meeting.repository.MeetingRepository;
import com.hoppy.app.member.domain.Member;
import com.hoppy.app.member.repository.MemberRepository;
import com.hoppy.app.search.dto.MeetingSearchListDto;
import com.hoppy.app.utils.EntityUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * packageName    : com.hoppy.app.meeting.service
 * fileName       : MeetingServiceTest
 * author         : Kim
 * date           : 2022-09-11
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022-09-11        Kim       최초 생성
 */
@SpringBootTest
@DisplayName("모임 통합 테스트")
public class MeetingServiceTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MeetingRepository meetingRepository;

    @Autowired
    MeetingService meetingService;

    @AfterEach
    void clean() {
        meetingRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @DisplayName("🛠️ 모임 수정 테스트")
    @Test
    void updateMeetingTest() {
        // given
        Member member = memberRepository.save(EntityUtil.testMember(1L));
        Meeting meeting = meetingRepository.save(EntityUtil.testMeeting(member, Category.HEALTH));

        // when
        UpdateMeetingDto updateMeetingDto = new UpdateMeetingDto("update-title", "update-content", null);
        meetingService.updateMeeting(updateMeetingDto, member.getId(), meeting.getId());

        // then
        meeting = meetingService.findById(meeting.getId());
        assertThat(meeting.getTitle()).isEqualTo("update-title");
        assertThat(meeting.getContent()).isEqualTo("update-content");
        assertThat(meeting.getUrl()).isNotNull();
    }

    @DisplayName("🔎 모임 검색 테스트")
    @Test
    void searchMeetingTest() {
        // given
        // noting

        // when
        MeetingSearchListDto dto = meetingService.searchMeetings("title", 0);

        // then
        assertThat(dto.getNextPagingUrl()).isEqualTo("end");
    }
}
