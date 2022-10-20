package com.hoppy.app.member.dto;

import com.hoppy.app.member.domain.MemberMeeting;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyMeetingsDto {

    private Long id;

    private String title;

    public static MyMeetingsDto meetingToDto(MemberMeeting meeting) {
        return MyMeetingsDto.builder()
                .id(meeting.getMeetingId())
                .title(meeting.getMeeting().getTitle())
                .build();
    }
}
