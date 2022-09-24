package com.hoppy.app.story.domain.story;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.hoppy.app.community.domain.Reply;
import com.hoppy.app.like.domain.MemberStoryLike;
import com.hoppy.app.member.domain.Member;
import com.hoppy.app.story.dto.UploadStoryDto;
import com.hoppy.app.story.domain.BaseTimeEntity;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
public class Story extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column
    private String filePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @Exclude
    private Member member;

    // TODO: cascade = CascadeType.REMOVE로 인한 삭제 시 N+1 문제 고려
//    @OneToMany(mappedBy = "story", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @OneToMany(mappedBy = "story", fetch = FetchType.LAZY)
    @BatchSize(size = 100)
    @Default
    @Exclude
    private Set<StoryReply> replies = new HashSet<>();

//    @OneToMany(mappedBy = "story", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @OneToMany(mappedBy = "story", fetch = FetchType.LAZY)
    @BatchSize(size = 100)
    @Default
    @Exclude
    private Set<MemberStoryLike> likes = new HashSet<>();

    public static Story of(UploadStoryDto dto, Member member) {
        return Story.builder()
                .member(member)
                .title(dto.getTitle())
                .content(dto.getContent())
                .filePath(dto.getFilename())
                .build();
    }
}
