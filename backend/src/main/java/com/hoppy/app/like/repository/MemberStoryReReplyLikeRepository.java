package com.hoppy.app.like.repository;

import com.hoppy.app.like.domain.MemberStoryReReplyLike;
import com.hoppy.app.member.domain.Member;
import com.hoppy.app.story.domain.story.StoryReReply;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberStoryReReplyLikeRepository extends JpaRepository<MemberStoryReReplyLike, Long> {

    List<MemberStoryReReplyLike> findAllByMember(Member member);

    @Query("select s from MemberStoryReReplyLike s where s.member.id = :memberId and s.reReply.id = :reReplyId")
    Optional<MemberStoryReReplyLike> findByMemberIdAndReReplyId(
            @Param("memberId") Long memberId,
            @Param("reReplyId") Long reReplyId
    );
    @Modifying
    @Query("delete from MemberStoryReReplyLike s where s.member.id = :memberId and s.reReply.id = :reReplyId")
    void deleteByMemberIdAndReReplyId(
            @Param("memberId") Long memberId,
            @Param("reReplyId") Long reReplyId
    );

    @Modifying
    @Query("delete from MemberStoryReReplyLike s where s.id in :idList")
    void deleteAllByList(@Param("idList") List<Long> idList);

    @Modifying
    @Query("delete from MemberStoryReReplyLike rr where rr.reReply.id =:id")
    void deleteReReplyLikes(@Param("id") Long id);
}
