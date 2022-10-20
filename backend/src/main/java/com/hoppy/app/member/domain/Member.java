package com.hoppy.app.member.domain;

import com.hoppy.app.like.domain.MemberMeetingLike;
import com.hoppy.app.like.domain.MemberPostLike;
import com.hoppy.app.like.domain.MemberReReplyLike;
import com.hoppy.app.like.domain.MemberReplyLike;
import com.hoppy.app.like.domain.MemberStoryLike;
import com.hoppy.app.login.auth.SocialType;
import com.hoppy.app.member.Role;
import com.hoppy.app.story.domain.story.Story;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.ToString.Exclude;
import org.hibernate.annotations.BatchSize;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Member {

    @Id
    private Long id;  //  == socialId

    private String email;
    private String username;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String profileImageUrl;

    private String intro;
    private String password;
    private boolean deleted;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    @BatchSize(size = 100)
    @Default
    @Exclude
    private Set<MemberMeeting> myMeetings = new HashSet<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    @BatchSize(size = 100)
    @Default
    @Exclude
    private Set<MemberMeetingLike> meetingLikes = new HashSet<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    @BatchSize(size = 100)
    @Default
    @Exclude
    private Set<MemberPostLike> postLikes = new HashSet<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    @BatchSize(size = 100)
    @Default
    @Exclude
    private Set<MemberReplyLike> replyLikes = new HashSet<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    @BatchSize(size = 100)
    @Default
    @Exclude
    private Set<MemberReReplyLike> reReplyLikes = new HashSet<>();
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member")
    @Default
    @Exclude
    private Set<Story> stories = new HashSet<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    @BatchSize(size = 100)
    @Default
    @Exclude
    private Set<MemberStoryLike> storyLikes = new HashSet<>();
}
