package com.hoppy.app.member.service;

import com.hoppy.app.member.domain.Member;
import com.hoppy.app.member.dto.UpdateMemberDto;
import com.hoppy.app.member.repository.MemberRepository;
import com.hoppy.app.response.error.exception.BusinessException;
import com.hoppy.app.response.error.exception.ErrorCode;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public Member findById(long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
    }

    @Override
    public Member findByIdWithPostLikes(long id) {
        return memberRepository.findByIdWithPostLikes(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
    }

    @Override
    public Member findByIdWithMeetingLikes(long id) {
        return memberRepository.findByIdWithMeetingLikes(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
    }

    @Override
    @Transactional
    public Member updateById(long memberId, UpdateMemberDto dto) {
        Member member = findById(memberId);
        if(member.isDeleted()) {
            throw new BusinessException(ErrorCode.DELETED_MEMBER);
        }
        if(dto.getUsername() != null) {
            if(existsByUsername(dto.getUsername()) && dto.getUsername() != member.getUsername()) {
                throw new BusinessException(ErrorCode.MEMBER_DUPLICATE);
            }
            member.setUsername(dto.getUsername());
        }
        if(dto.getIntro() != null) {
            member.setIntro(dto.getIntro());
        }
        if(dto.getProfileUrl() != null) {
            member.setProfileImageUrl(dto.getProfileUrl());
        }
        /*
        * 22.08.13 -tae
        * isPresent check 누락되어 수정하였음
        * jpa dirty checking 으로 업데이트
        * */
        return member;
    }

    @Override
    public boolean existsByUsername(String username) {
        return memberRepository.existsMemberByUsername(username);
    }

    @Override
    public Member deleteById(long id) {
        Member member = findById(id);
        if(member.isDeleted()) {
            throw new BusinessException(ErrorCode.DELETED_MEMBER);
        } else {
            member.setDeleted(true);
            memberRepository.save(member);
        }
        return member;
    }

    @Override
    public List<Member> infiniteScrollPagingMember(List<Long> memberIdList, long lastId,
            PageRequest pageRequest) {
        return memberRepository.infiniteScrollPagingMember(memberIdList, lastId, pageRequest);
    }

    @Override
    public List<Long> getMeetingLikes(long memberId) {
        Member member = findByIdWithMeetingLikes(memberId);
        return member.getMeetingLikes().stream()
                .map(M -> M.getMeeting().getId())
                .collect(Collectors.toList());
    }

    @Override
    public boolean checkMeetingLiked(long memberId, long meetingId) {
        Member member = findByIdWithMeetingLikes(memberId);
        return member.getMeetingLikes().stream()
                .anyMatch(M -> M.getMeetingId() == meetingId);
    }


}
