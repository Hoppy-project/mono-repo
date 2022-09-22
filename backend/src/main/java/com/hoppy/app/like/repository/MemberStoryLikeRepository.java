package com.hoppy.app.like.repository;

import com.hoppy.app.like.domain.MemberStoryLike;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberStoryLikeRepository extends JpaRepository<MemberStoryLike, Long> {

    @Query("select s from MemberStoryLike s where s.member.id = :memberId and s.story.id = :storyId")
    Optional<MemberStoryLike> findByMemberIdAndStoryId(
            @Param("memberId") Long memberId,
            @Param("storyId") Long storyId
    );

    @Modifying
    @Query("delete from MemberStoryLike s where s.member.id = :memberId and s.story.id = :storyId")
    void deleteByMemberIdAndStoryId(
            @Param("memberId") Long memberId,
            @Param("storyId") Long storyId
    );

    @Modifying
    @Query("delete from MemberStoryLike s where s.id in :idList")
    void deleteAllByList(@Param("idList") List<Long> idList);
}
