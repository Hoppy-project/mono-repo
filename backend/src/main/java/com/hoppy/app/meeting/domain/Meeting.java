package com.hoppy.app.meeting.domain;

import com.hoppy.app.meeting.Category;
import com.hoppy.app.member.domain.MemberMeeting;
import com.hoppy.app.member.domain.MemberMeetingLike;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Meeting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder.Default
    private String url = "none";

    @Column(nullable = false) // length 옵션 추가 논의 필요
    private String title;

    @Column(nullable = false) // length 옵션 추가 논의 필요
    private String content;

    @Column(nullable = false)
    private Category category;

    @Column(nullable = false)
    private Integer memberLimit;

    @OneToMany(mappedBy = "meeting", fetch = FetchType.LAZY)
    @BatchSize(size = 100)
    @Builder.Default
    private Set<MemberMeeting> participants = new HashSet<>();

//  batch size를 설정하여 DB 성능 이슈(N + 1)가 발생하는 것을 방지할 수 있다.
    @OneToMany(mappedBy = "meeting", fetch = FetchType.LAZY)
    @BatchSize(size = 100)
    @Builder.Default
    private Set<MemberMeetingLike> myMeetingLikes = new HashSet<>();
}
