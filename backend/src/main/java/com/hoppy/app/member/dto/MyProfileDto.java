package com.hoppy.app.member.dto;

import com.hoppy.app.like.domain.MemberMeetingLike;
import com.hoppy.app.member.domain.Member;
import com.hoppy.app.member.domain.MemberMeeting;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class MyProfileDto {

    private Long id;
    private String email;
    private String username;
    private String profileUrl;
    private String intro;
    private boolean deleted;
    private List<Long> myMeetingsId;
    private List<Long> likeMeetingsId;

    public static MyProfileDto of(Member member) {

        return MyProfileDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .username(member.getUsername())
                .profileUrl(member.getProfileImageUrl())
                .intro(member.getIntro())
                .deleted(member.isDeleted())
                .myMeetingsId(member.getMyMeetings().stream().map(MemberMeeting::getMeetingId).collect(
                        Collectors.toList()))
                .likeMeetingsId(member.getMeetingLikes().stream().map(MemberMeetingLike::getMeetingId).collect(
                        Collectors.toList()))
                .build();
    }

}