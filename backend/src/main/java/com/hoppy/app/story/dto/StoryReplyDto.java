package com.hoppy.app.story.dto;

import com.hoppy.app.story.domain.story.StoryReReply;
import com.hoppy.app.story.domain.story.StoryReply;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StoryReplyDto {

    private Long id;

    private String profileUrl;

    private String username;

    private String content;

    private LocalDateTime createdDate;

    private int likeCount;

    private int reReplyCount;

    private List<StoryReReplyDto> reReplies;

    public static StoryReplyDto of(StoryReply storyReply) {
        return StoryReplyDto.builder()
                .id(storyReply.getId())
                .profileUrl(storyReply.getMember().getProfileImageUrl())
                .username(storyReply.getMember().getUsername())
                .content(storyReply.getContent())
                .createdDate(storyReply.getCreatedDate())
                .likeCount(storyReply.getLikes().size())
                .reReplyCount(storyReply.getReReplies().size())
                .reReplies(storyReply.getReReplies().stream().map(StoryReReplyDto::of).collect(
                        Collectors.toList()))
                .build();
    }
}
