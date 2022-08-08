package com.hoppy.app.meeting.domain;

import com.hoppy.app.community.domain.Post;
import com.hoppy.app.meeting.Category;
import com.hoppy.app.meeting.dto.CreateMeetingDto;
import com.hoppy.app.member.domain.MemberMeeting;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
public class Meeting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long ownerId;

    @Column(nullable = false)
    @Builder.Default
    private String url = "default-url";

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(nullable = false)
    private Integer memberLimit;

    @Column
    @Setter
    @Builder.Default
    private Boolean fullFlag = false;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "meetingId")
    @Builder.Default
    @Exclude
    @BatchSize(size = 20)
    private Set<MemberMeeting> participants = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "meeting")
    @Builder.Default
    @Exclude
    Set<Post> posts = new HashSet<>();

    public Boolean isFull() {
        return this.fullFlag;
    }

    public static Meeting of(CreateMeetingDto dto, Long ownerId) {
        return Meeting.builder()
                .ownerId(ownerId)
                .url("https://hoppyservice.s3.ap-northeast-2.amazonaws.com/" + dto.getFilename())
                .title(dto.getTitle())
                .content(dto.getContent())
                .memberLimit(dto.getMemberLimit())
                .category(Category.intToCategory(dto.getCategory()))
                .build();
    }
}
