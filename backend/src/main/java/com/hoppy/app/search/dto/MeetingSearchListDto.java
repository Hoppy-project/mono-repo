package com.hoppy.app.search.dto;

import com.hoppy.app.meeting.domain.Meeting;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * packageName    : com.hoppy.app.search.dto
 * fileName       : MeetingSearchDto
 * author         : Kim
 * date           : 2022-09-16
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022-09-16        Kim       최초 생성
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeetingSearchListDto {

    private List<MeetingSearchDto> meetingList;

    private String nextPagingUrl;

    public static MeetingSearchListDto of(List<MeetingSearchDto> meetingList, String nextPagingUrl) {
        return MeetingSearchListDto.builder()
                .meetingList(meetingList)
                .nextPagingUrl(nextPagingUrl)
                .build();
    }
}