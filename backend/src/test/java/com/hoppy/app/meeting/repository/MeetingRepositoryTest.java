package com.hoppy.app.meeting.repository;

import com.hoppy.app.meeting.Category;
import com.hoppy.app.meeting.domain.Meeting;
import java.util.List;

import com.hoppy.app.member.domain.Member;
import com.hoppy.app.member.repository.MemberRepository;
import com.hoppy.app.utils.EntityUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

@DataJpaTest
@TestInstance(Lifecycle.PER_CLASS)
class MeetingRepositoryTest {

    @Autowired
    MeetingRepository meetingRepository;

    @Autowired
    MemberRepository memberRepository;

    @BeforeAll
    void beforeAll() {
        meetingRepository.deleteAll();
        memberRepository.deleteAll();
        /*
        * 20개의 모임을 생성한다
        * HEALTH 카테고리 모임은 0과 짝수번째에 생성되며 총 10개가 생성된다.
        * 그 외에는 LIFE 카테고리 모임이 생성된다.
        * */
        Member owner = memberRepository.save(EntityUtil.testMember(1L));
        for(int i = 0; i < 20; i++) {
            if(i % 2 == 0) meetingRepository.save(EntityUtil.testHealthMeeting(owner));
            else meetingRepository.save(EntityUtil.testArtMeeting(owner));
        }
    }

    @AfterAll
    void after() {
        meetingRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @DisplayName("infiniteScrollPaging 테스트")
    @Test
    void infiniteScrollPagingTest() {
        /*
        * 무한 스크롤 페이지네이션 요청은 기본적으로 14개씩 요청되지만
        * 현재 HEALTH 모임이 10개 밖에 없으므로 응답 결과 모임의 개수는 10개이다.
        * */
        List<Meeting> result = meetingRepository.infiniteScrollPaging(Category.HEALTH, Long.MAX_VALUE, PageRequest.of(0, 14));
        Assertions.assertThat(result.size()).isEqualTo(10);
    }


}