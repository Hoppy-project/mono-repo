package com.hoppy.app.community.dto;

import com.hoppy.app.community.domain.Post;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author 태경 2022-08-09
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PostDetailDto {

    private long id;

    private String authorName;

    private String profileImageUrl;

    private String title;

    private String content;

    private String url;

    private boolean liked;

    private int likeCount;

    private int replyCount;

    private List<ReplyDto> replies;

    public static PostDetailDto of(Post post, boolean liked, int likeCount, int replyCount, List<ReplyDto> replies) {
        return PostDetailDto.builder()
                .id(post.getId())
                .profileImageUrl(post.getAuthor().getProfileImageUrl())
                .authorName(post.getAuthor().getUsername())
                .title(post.getTitle())
                .content(post.getContent())
                .url(post.getUrl())
                .liked(liked)
                .likeCount(likeCount)
                .replyCount(replyCount)
                .replies(replies)
                .build();
    }
}
