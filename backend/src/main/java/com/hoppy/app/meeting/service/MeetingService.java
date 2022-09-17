package com.hoppy.app.meeting.service;

import com.hoppy.app.meeting.Category;
import com.hoppy.app.meeting.domain.Meeting;
import com.hoppy.app.meeting.dto.*;
import com.hoppy.app.member.domain.Member;
import com.hoppy.app.search.dto.MeetingSearchListDto;

import java.util.List;

public interface MeetingService {

    void createAndSaveMemberMeetingData(Meeting meeting, Member member);
    boolean checkTitleDuplicate(String name);
    Meeting createMeeting(CreateMeetingDto dto, long ownerId);
    Meeting findById(long id);
    Meeting findByIdWithParticipants(long id);
    void updateMeeting(UpdateMeetingDto dto, long memberId, long meetingId);
    void setPremium(long memberId, long meetingId);
    void changeMemberLimit(long memberId, long meetingId, int memberLimit);

    void withdrawMeeting(long meetingId, long memberId);
    void checkJoinRequestValid(long meetingId, long memberId);
    void checkJoinedMemberV1(List<Member> participants, long memberId);
    void checkJoinedMemberV2(List<ParticipantDto> participants, long memberId);
    long validCheckLastId(long lastId);
    long getLastId(List<Meeting> meetingList);
    String createNextSearchPagingUrl(int categoryNumber, long lastId, int size);
    List<Meeting> pagingMeetingList(Category category, long lastId);
    List<MeetingDto> listToDtoList(List<Meeting> meetingList, long memberId);
    PagingMeetingDto pagingMeeting(int categoryNumber, long lastId, long memberId);

    List<Member> getParticipantList(Meeting meeting);
    List<ParticipantDto> getParticipantDtoList(Meeting meeting);

    void likeMeeting(long memberId, long meetingId);
    void dislikeMeeting(long memberId, long meetingId);
    String createNextSearchPagingUrl(String keyword, long lastId, int size);
    MeetingSearchListDto searchMeetings(String keyword, long lastId);
}
