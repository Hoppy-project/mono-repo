package com.hoppy.app.member.dto;

import com.hoppy.app.like.domain.MemberMeetingLike;
import com.hoppy.app.meeting.domain.Meeting;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikeMeetingsDto {

    private Long id;

    private String title;
    public static LikeMeetingsDto meetingToDto(MemberMeetingLike meeting) {
        return LikeMeetingsDto.builder()
                .id(meeting.getMeetingId())
                .title(meeting.getMeeting().getTitle())
                .build();
    }
}
