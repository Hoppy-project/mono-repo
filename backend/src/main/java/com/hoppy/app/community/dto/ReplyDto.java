package com.hoppy.app.community.dto;

import com.hoppy.app.community.domain.Reply;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author 태경 2022-08-09
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReplyDto {

    private long id;

    private String authorProfileUrl;

    private String authorName;

    private String content;

    private boolean liked;

    private int likeCount;

    private List<ReReplyDto> replies;

    public static ReplyDto of(Reply reply) {
        return ReplyDto.builder()
                .id(reply.getId())
                .authorProfileUrl(reply.getAuthor().getProfileImageUrl())
                .authorName(reply.getAuthor().getUsername())
                .content(reply.getContent())
                .build();
    }
}
