package com.hoppy.app.meeting.repository;

import com.hoppy.app.meeting.Category;
import com.hoppy.app.meeting.domain.Meeting;
import java.util.List;
import java.util.Optional;
import javax.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    /*
     * 페이징 기능은 14개씩 데이터를 조회하고 where를 사용해서 no-offset하게 구현한다.
     * 또한 다음 조회 link를 함께 제공하여 좀 더 restful한 api를 제공한다.
     * offset은 필요없는 row까지 읽어오고 건너뛰는 과정에서 성능 저하가 발생하지만
     * where는 필요한 index부터 조회하기 때문에 성능 저하가 발생하지 않는다.
     *
     * myMeetingLikes를 fetch해서 가져올 필요가 있을까?
     * 이렇게 가져오는 이유는 현재 사용자가 해당 모임에 좋아요를 눌렀는지 확인하기 위함이다.
     * 이렇게 하지말고 해당 유저가 좋아요 누른 모임 목록을 따로 불러와서
     * 모임에 좋아요를 눌렀는지 확인하는 것이 성능상 이점이 있을 것으로 보인다.
     * stream과 filter를 사용해보자.
     * */
    @Query("select m from Meeting as m where m.category = :category and m.id <= :lastId order by m.id desc")
    List<Meeting> infiniteScrollPaging(@Param("category") Category category, @Param("lastId") Long lastId, Pageable pageable);

    Optional<Meeting> findByTitle(String title);

    /*
    * FOR UPDATE is not allowed in DISTINCT or grouped select
    * */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select m from Meeting m where m.id = :id")
    Optional<Meeting> findByIdUsingLock(@Param("id") Long id);

    @Query("select distinct m from Meeting m left join fetch m.participants where m.id = :id")
    Optional<Meeting> findWithParticipantsById(@Param("id") Long id);

    List<Meeting> findByTitleContains(String title);

    @Query("select m from Meeting m where m.title like %:keyword% and m.id <= :lastId order by m.id desc")
    List<Meeting> findByTitleLikes(@Param("keyword") String keyword, @Param("lastId") Long lastId, Pageable pageable);
}
