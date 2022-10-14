package com.hoppy.app.story.dto;

import com.hoppy.app.story.domain.story.Story;
import java.time.LocalDateTime;
import java.util.List;
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

/**
 * 페이지네이션 시 간단하게 보여줄 총 좋아요/댓글 수를 포함한 스토리 Dto
 */

public class StoryDto {

    private Long id;

    private Long memberId;

    private String profileUrl;

    private String username;

    private String title;

    private String filename;

    private String content;

    private boolean liked;

    private int likeCount;

    private int replyCount;

    private LocalDateTime createdDate;

    public static StoryDto of(Story story) {
        return StoryDto.builder()
                .id(story.getId())
                .memberId(story.getMember().getId())
                .profileUrl(story.getMember().getProfileImageUrl())
                .username(story.getMember().getUsername())
                .title(story.getTitle())
                .content(story.getContent())
                .filename(story.getFilePath())
                .likeCount(story.getLikes().size())
                .replyCount(story.getReplies().size())
                .createdDate(story.getCreatedDate())
                .build();
    }

    void setLiked(StoryDto dto, boolean liked) {
        dto.setLiked(liked);
    }
}
