package com.hoppy.app.member.repository;

import com.hoppy.app.meeting.domain.Meeting;
import com.hoppy.app.member.domain.Member;
import com.hoppy.app.member.domain.MemberMeeting;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberMeetingRepository extends JpaRepository<MemberMeeting, Long> {

    @Modifying
    @Query("delete from MemberMeeting m where m.meeting = :meeting and m.member = :member")
    void deleteMemberMeetingByMeetingAndMember(@Param("meeting") Meeting meeting, @Param("member") Member member);

    List<MemberMeeting> findALlByMeeting(Meeting meeting);

    List<MemberMeeting> findAllByMember(Member member);
}
