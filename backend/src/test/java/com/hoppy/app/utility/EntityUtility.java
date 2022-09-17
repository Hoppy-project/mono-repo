package com.hoppy.app.utility;

import com.hoppy.app.community.domain.Post;
import com.hoppy.app.community.domain.ReReply;
import com.hoppy.app.community.domain.Reply;
import com.hoppy.app.meeting.Category;
import com.hoppy.app.meeting.domain.Meeting;
import com.hoppy.app.member.domain.Member;

import java.util.UUID;

/**
 * packageName    : com.hoppy.app.utility
 * fileName       : Utility
 * author         : Kim
 * date           : 2022-08-29
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022-08-29        Kim       최초 생성
 */
public class EntityUtility {

    public static Member testMember(long id) {
        return Member.builder()
                .id(id)
                .username("test-name")
                .build();
    }
    public static Meeting testMeeting(Member owner, String url, String title, String content, Category category, Integer limit) {
        return Meeting.builder()
                .owner(owner)
                .url(url)
                .title(title)
                .content(content)
                .category(category)
                .memberLimit(limit)
                .build();
    }
    public static Meeting testMeeting(Member owner, Category category) {
        return testMeeting(owner,
                "test-url",
                "test-title-" + UUID.randomUUID().toString().substring(0,5),
                "test-content",
                category,
                10
        );
    }
    public static Meeting testHealthMeeting(Member owner) {
        return testMeeting(owner, Category.HEALTH);
    }
    public static Meeting testArtMeeting(Member owner) {
        return testMeeting(owner, Category.ART);
    }

    public static Post testPost(Member author) {
        return Post.builder()
                .title("test-title")
                .content("test-content")
                .imageUrl("test-imageUrl")
                .author(author)
                .build();
    }
    public static Post testPost(Member author, Meeting meeting, String title, String content) {
        return Post.builder()
                .title(title)
                .content(content)
                .meeting(meeting)
                .author(author)
                .build();
    }

    public static Reply testReply(Member author) {
        return Reply.builder()
                .content("test-content")
                .author(author)
                .build();
    }
    public static Reply testReply(Member author, Post post, String content) {
        return Reply.builder()
                .content(content)
                .post(post)
                .author(author)
                .build();
    }

    public static ReReply testReReply(Member author) {
        return ReReply.builder()
                .content("test-content")
                .author(author)
                .build();
    }
    public static ReReply testReReply(Member author, Reply reply, String content) {
        return ReReply.builder()
                .content(content)
                .reply(reply)
                .author(author)
                .build();
    }
}
