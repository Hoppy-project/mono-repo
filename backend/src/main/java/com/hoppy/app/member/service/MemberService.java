package com.hoppy.app.member.service;

import com.hoppy.app.member.domain.Member;
import com.hoppy.app.member.dto.MyProfileDto;
import com.hoppy.app.member.dto.UpdateMemberDto;
import java.util.List;
import org.springframework.data.domain.PageRequest;

public interface MemberService {

    public Member findById(long id);

    public Member findByIdWithPostLikes(long id);

    public Member findByIdWithMeetingLikes(long id);

    public Member updateById(long memberId, UpdateMemberDto memberDto);

    public Member deleteById(long id);

    public List<Member> infiniteScrollPagingMember(List<Long> memberIdList, long lastId, PageRequest pageRequest);

    public List<Long> getMeetingLikes(long memberId);

    public boolean checkMeetingLiked(long memberId, long meetingId);

    public boolean existsByUsername(String username);

    public MyProfileDto getMyProfileInfo(Long memberId);
}
