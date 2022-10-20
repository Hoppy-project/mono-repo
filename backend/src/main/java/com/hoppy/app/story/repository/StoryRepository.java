package com.hoppy.app.story.repository;

import com.hoppy.app.member.domain.Member;
import com.hoppy.app.story.domain.story.Story;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StoryRepository extends JpaRepository<Story, Long> {


    /**
     * 마이 프로필에서 보여지는 스토리
     */
    List<Story> findByMemberIdOrderByIdDesc(Long memberId);

    /**
     * [취미 스토리]에서 보이는 모든 사용자들의 스토리, 최신순 (storyId 내림차순)으로 3개씩 페이지네이션
     */
//    @Query("select s from Story as s join fetch s.member where s.id < :lastId order by s.id desc")
    @Query("select s from Story as s where s.id < :lastId order by s.id desc")
    List<Story> findNextStoryOrderByIdDesc(@Param("lastId") Long lastId, Pageable pageable);
}
