package com.hoppy.app.story.repository;

import com.hoppy.app.like.domain.MemberStoryLike;
import com.hoppy.app.story.domain.story.StoryReply;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StoryReplyRepository extends JpaRepository<StoryReply, Long> {

//    @Query("select s from StoryReply s where s.story.id = :storyId and s.id = :replyId")
//    Optional<StoryReply> findByStoryIdAndReplyId(
//            @Param("storyId") Long storyId,
//            @Param("replyId") Long replyId
//    );

    @Query("select s from StoryReply s where s.id = :id and s.member.id = :memberId")
    Optional<StoryReply> findByIdAndMemberId(
            @Param("id") Long id,
            @Param("memberId") Long memberId
    );

    @Modifying
    @Query("delete from StoryReply s where s.id in :idList")
    void deleteAllByList(@Param("idList") List<Long> idList);

    @Query("select s from StoryReply s "
            + "left join fetch s.reReplies "
            + "where s.id = :id and s.member.id = :memberId")
    Optional<StoryReply> findByIdAndMemberIdWithReReplies(
            @Param("id") Long id,
            @Param("memberId") Long memberId
    );

    @Modifying
    @Query("delete from StoryReply s where s.story.id =:id")
    void deleteStoryReplyByStoryId(@Param("id") Long id);

    @Modifying
    @Query("delete from StoryReReply rr where rr.reply.id =:id")
    void deleteReReplies(@Param("id") Long id);
}
