package com.hoppy.app.meeting.dto;

import com.hoppy.app.meeting.domain.Meeting;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author 태경 2022-07-18
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeetingDetailDto {

    private Long id;

    private String profileImageUrl;

    private String ownerName;

    private String title;

    private String content;

    private String url;

    private Boolean liked;

    private List<ParticipantDto> participantList;

    public static MeetingDetailDto of(Meeting meeting, List<ParticipantDto> participantList, Boolean liked) {
        return MeetingDetailDto.builder()
                .id(meeting.getId())
                .profileImageUrl(meeting.getOwner().getProfileImageUrl())
                .ownerName(meeting.getOwner().getUsername())
                .title(meeting.getTitle())
                .content(meeting.getContent())
                .url(meeting.getUrl())
                .liked(liked)
                .participantList(participantList)
                .build();
    }
}
