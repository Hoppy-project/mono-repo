package com.hoppy.app.story.dto;

import com.hoppy.app.story.domain.story.Story;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
/**
 * (스토리 클릭 시 나타날) 스토리의 모든 정보를 반환하는 Dto
 */
public class StoryDetailDto {

    private Long id;

    private String profileUrl;

    private String title;

    private String content;

    private String username;

    private String filename;

    private boolean liked;

    private int likeCount;

    private int replyCount;

    private LocalDateTime createdDate;

    private List<StoryReplyDto> replies;

    public static StoryDetailDto from(Story story) {
        return StoryDetailDto.builder()
                .id(story.getId())
                .profileUrl(story.getMember().getProfileImageUrl())
                .title(story.getTitle())
                .content(story.getContent())
                .username(story.getMember().getUsername())
                .likeCount(story.getLikes().size())
                .replyCount(story.getReplies().size())
                .filename(story.getFilePath())
                .createdDate(story.getCreatedDate())
                .replies(story.getReplies().stream().map(StoryReplyDto::of).collect(Collectors.toList()))
                .build();
    }
}