package com.hoppy.app.search.dto;

import com.hoppy.app.meeting.domain.Meeting;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : com.hoppy.app.search.dto
 * fileName       : MeetingSearchDto
 * author         : Kim
 * date           : 2022-09-17
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022-09-17        Kim       최초 생성
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeetingSearchDto {

    private Long id;

    private String url;

    private String title;

    private String content;

    public static MeetingSearchDto meetingToMeetingSearchDto(Meeting meeting) {
        return MeetingSearchDto.builder()
                .id(meeting.getId())
                .url(meeting.getUrl())
                .title(meeting.getTitle())
                .content(meeting.getContent())
                .build();
    }
}
