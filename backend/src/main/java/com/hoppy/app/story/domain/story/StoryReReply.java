package com.hoppy.app.story.domain.story;

import com.hoppy.app.like.domain.MemberReReplyLike;
import com.hoppy.app.like.domain.MemberStoryReReplyLike;
import com.hoppy.app.member.domain.Member;
import com.hoppy.app.story.domain.BaseTimeEntity;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class StoryReReply extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Setter
    private String content;

    @OneToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne
    @Exclude
    private StoryReply reply;

    @OneToMany(mappedBy = "reReply", fetch = FetchType.LAZY)
    @BatchSize(size = 100)
    @Default
    @Exclude
    private Set<MemberStoryReReplyLike> likes = new HashSet<>();
}
