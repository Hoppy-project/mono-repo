package com.hoppy.app.like.repository;

import com.hoppy.app.like.domain.MemberPostLike;
import com.hoppy.app.like.domain.MemberReplyLike;
import com.hoppy.app.like.domain.MemberStoryReplyLike;
import com.hoppy.app.member.domain.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberStoryReplyLikeRepository extends JpaRepository<MemberStoryReplyLike, Long> {

    List<MemberStoryReplyLike> findAllByMember(Member member);

    @Query("select s from MemberStoryReplyLike s where s.member.id = :memberId and s.reply.id = :replyId")
    Optional<MemberStoryReplyLike> findByMemberIdAndReplyId(
            @Param("memberId") Long memberId,
            @Param("replyId") Long replyId
    );

    @Modifying
    @Query("delete from MemberStoryReplyLike s where s.member.id = :memberId and s.reply.id = :replyId")
    void deleteByMemberIdAndReplyId(
            @Param("memberId") Long memberId,
            @Param("replyId") Long replyId
    );

    @Modifying
    @Query("delete from MemberStoryReplyLike s where s.id in :idList")
    void deleteAllByList(@Param("idList") List<Long> idList);
}
