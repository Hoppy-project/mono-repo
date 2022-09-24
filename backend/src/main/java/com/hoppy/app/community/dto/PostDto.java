package com.hoppy.app.community.dto;

import com.hoppy.app.community.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author 태경 2022-08-05
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PostDto {
    /*
    * 커뮤니티 게시물 목록 조회 시 사용되는 Dto 입니다. -tae
    * */
    private long id;

    private String profileImageUrl;

    private String ownerName;

    private String title;

    private String content;

    private String url;

    private boolean liked;

    private int likeCount;

    private int replyCount;

    public static PostDto postToPostDto(Post post, boolean liked, int likeCount, int replyCount) {
        return PostDto.builder()
                .id(post.getId())
                .profileImageUrl(post.getAuthor().getProfileImageUrl())
                .ownerName(post.getAuthor().getUsername())
                .title(post.getTitle())
                .content(post.getContent())
                .url(post.getUrl())
                .liked(liked)
                .likeCount(likeCount)
                .replyCount(replyCount)
                .build();
    }
}
