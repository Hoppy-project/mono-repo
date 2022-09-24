package com.hoppy.app.meeting.service;

import com.hoppy.app.like.domain.MemberMeetingLike;
import com.hoppy.app.like.repository.MemberMeetingLikeRepository;
import com.hoppy.app.meeting.Category;
import com.hoppy.app.meeting.domain.Meeting;
import com.hoppy.app.meeting.dto.*;
import com.hoppy.app.meeting.repository.MeetingRepository;
import com.hoppy.app.member.domain.Member;
import com.hoppy.app.member.domain.MemberMeeting;
import com.hoppy.app.member.repository.MemberMeetingRepository;
import com.hoppy.app.member.service.MemberService;
import com.hoppy.app.response.error.exception.BusinessException;
import com.hoppy.app.response.error.exception.ErrorCode;

import java.util.*;
import java.util.stream.Collectors;

import com.hoppy.app.search.dto.MeetingSearchDto;
import com.hoppy.app.search.dto.MeetingSearchListDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MeetingServiceImpl implements MeetingService {

    private final MemberService memberService;
    private final MeetingRepository meetingRepository;
    private final MemberMeetingRepository memberMeetingRepository;
    private final MemberMeetingLikeRepository memberMeetingLikeRepository;

    @Override
    @Transactional
    public void createAndSaveMemberMeetingData(Meeting meeting, Member member) {
        memberMeetingRepository.save(MemberMeeting.builder()
                .meeting(meeting)
                .member(member)
                .build());
    }

    @Override
    @Transactional
    public Meeting createMeeting(CreateMeetingDto dto, Member owner) throws BusinessException {
        if(checkTitleDuplicate(dto.getTitle())) {
            throw new BusinessException(ErrorCode.TITLE_DUPLICATE);
        }
        return meetingRepository.save(Meeting.of(dto, owner));
    }

    @Override
    public boolean checkTitleDuplicate(String title) {
        return meetingRepository.findByTitle(title).isPresent();
    }

    @Override
    @Transactional
    public void withdrawMeeting(long meetingId, long memberId) {
        /*
        * [error] FOR UPDATE is not allowed in DISTINCT or grouped select
            - 베타적 락을 걸기 위해 update for 문과 fetch join & distinct 문을 함께 사용하다가 해당 에러를 만났음
            - 이는 업데이트를 위해서 다수 테이블과 행에 락을 걸어야 하는 행위이므로 허용되지 않는 쿼리문인 것으로 보임
            - 따라서 단일 Entity만 조회한 후 Batch size로 N + 1 문제가 발생하지 않도록 컬렉션 조인을 수행하였음
        * */
        Meeting meeting = meetingRepository.findByIdUsingLock(meetingId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEETING_NOT_FOUND));
        Member member = memberService.findById(memberId);

        if(meeting.getOwner().getId() == member.getId()) {
            throw new BusinessException(ErrorCode.OWNER_WITHDRAW_ERROR);
        }

        Set<MemberMeeting> participants = meeting.getParticipants();
        boolean joined = participants.stream().anyMatch(M -> Objects.equals(M.getMemberId(), memberId));
        if(!joined) {
            throw new BusinessException(ErrorCode.NOT_JOINED);
        }

        if(meeting.isFull()) {
            meeting.setFullFlag(false);
        }
        memberMeetingRepository.deleteMemberMeetingByMeetingAndMember(meeting, member);
    }

    @Override
    public List<Meeting> pagingMeetingList(Category category, long lastId) {
        return meetingRepository.infiniteScrollPaging(category, lastId, PageRequest.of(0, 14));
    }

    @Override
    public void checkJoinedMemberV1(List<Member> participants, long memberId) {
        boolean joined = participants.stream().anyMatch(P -> Objects.equals(P.getId(), memberId));
        if(!joined) {
            throw new BusinessException(ErrorCode.NOT_JOINED);
        }
    }

    @Override
    public void checkJoinedMemberV2(List<ParticipantDto> participants, long memberId) {
        boolean joined = participants.stream().anyMatch(P -> Objects.equals(P.getId(), memberId));
        if(!joined) {
            throw new BusinessException(ErrorCode.NOT_JOINED);
        }
    }

    @Override
    public long getLastId(List<Meeting> meetingList) {

        if(meetingList.isEmpty()) return 0;

        return meetingList.get(meetingList.size() - 1).getId() - 1;
    }

    @Override
    public long validCheckLastId(long lastId) {
        return (lastId == 0 ? Long.MAX_VALUE : lastId);
    }

    @Override
    public String createNextSearchPagingUrl(int categoryNumber, long lastId, int size) {
        if(lastId > 0 && size > 0)
            return "https://hoppy.kro.kr/api/meeting?categoryNumber=" + categoryNumber + "&lastId=" + lastId;
        return "end";
    }

    @Override
    public List<MeetingDto> listToDtoList(List<Meeting> meetingList, long memberId) {
        if(meetingList.isEmpty()) {
            return new ArrayList<>();
        }

        List<Long> meetingLikes = memberService.getMeetingLikes(memberId);

        Map<Long, Boolean> likedMap = meetingLikes.stream()
                .collect(Collectors.toMap(L -> L, L -> Boolean.TRUE));

        return meetingList.stream()
                .map(M -> MeetingDto.meetingToMeetingDto(M, likedMap.containsKey(M.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public PagingMeetingDto pagingMeeting(int categoryNumber, long lastId, long memberId) {
        lastId = validCheckLastId(lastId);
        Category category = Category.intToCategory(categoryNumber);
        List<Meeting> meetingList = pagingMeetingList(category, lastId);

        lastId = getLastId(meetingList);
        String nextPagingUrl = createNextSearchPagingUrl(categoryNumber, lastId, meetingList.size());
        List<MeetingDto> meetingDtoList = listToDtoList(meetingList, memberId);

        return PagingMeetingDto.of(meetingDtoList, nextPagingUrl);
    }

    @Override
    public Meeting findById(long id) {
        return meetingRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEETING_NOT_FOUND));
    }

    @Override
    public Meeting findByIdWithParticipants(long id) {
        return meetingRepository.findWithParticipantsById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEETING_NOT_FOUND));
    }

    @Override
    @Transactional
    public void updateMeeting(UpdateMeetingDto dto, long memberId, long meetingId) {
        Meeting meeting = findById(meetingId);

        if(meeting.getOwner().getId() != memberId) throw new BusinessException(ErrorCode.PERMISSION_ERROR);

        if(dto.getTitle() != null && !dto.getTitle().isEmpty()) meeting.setTitle(dto.getTitle());
        if(dto.getContent() != null && !dto.getContent().isEmpty()) meeting.setContent(dto.getContent());
        if(dto.getUrl() != null && !dto.getUrl().isEmpty()) meeting.setUrl(dto.getUrl());
    }

    @Override
    @Transactional
    public void setPremium(long memberId, long meetingId) {
        Meeting meeting = findById(meetingId);
        meeting.setPremium(true);
    }

    @Override
    @Transactional
    public void changeMemberLimit(long memberId, long meetingId, int memberLimit) {
        if(memberLimit > 300 || memberLimit < 2) throw new BusinessException(ErrorCode.MEMBER_LIMIT_RANGE_ERROR);

        Meeting meeting = findById(meetingId);

        if(!meeting.isPremium()) throw new BusinessException(ErrorCode.NOT_PREMIUM_ERROR);

        meeting.setMemberLimit(memberLimit);
    }

    @Override
    public List<Member> getParticipantList(Meeting meeting) {
        List<Long> memberIdList = meeting.getParticipants()
                .stream()
                .map(M -> M.getMember().getId())
                .sorted()
                .collect(Collectors.toList());

        return memberService.infiniteScrollPagingMember(memberIdList, 0L, PageRequest.of(0, memberIdList.size()));
    }

    @Override
    public List<ParticipantDto> getParticipantDtoList(Meeting meeting) {
        return getParticipantList(meeting)
                .stream()
                .map(M -> ParticipantDto.memberToParticipantDto(M, meeting.getOwner().getId()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void likeMeeting(long memberId, long meetingId) {
        boolean alreadyLiked = memberMeetingLikeRepository.findByMemberIdAndMeetingId(memberId, meetingId).isPresent();
        if(alreadyLiked) return;

        Meeting meeting = findById(meetingId);
        Member member = memberService.findById(memberId);
        memberMeetingLikeRepository.save(MemberMeetingLike.of(member, meeting));
    }

    @Override
    @Transactional
    public void dislikeMeeting(long memberId, long meetingId) {
        memberMeetingLikeRepository.deleteByMemberIdAndMeetingId(memberId, meetingId);
    }

    @Override
    public String createNextSearchPagingUrl(String keyword, long lastId, int size) {
        if(lastId > 0 && size > 0)
            return "https://hoppy.kro.kr/api/meeting/search/" + keyword + "&lastId=" + lastId;
        return "end";
    }

    @Override
    @Transactional(readOnly = true)
    public MeetingSearchListDto searchMeetings(String keyword, long lastId) {
        lastId = validCheckLastId(lastId);
        List<Meeting> meetingList = meetingRepository.findByTitleLikes(keyword, lastId, PageRequest.of(0, 14));

        lastId = getLastId(meetingList);
        String nextPagingUrl = createNextSearchPagingUrl(keyword, lastId, meetingList.size());
        List<MeetingSearchDto> meetingSearchDtos = meetingList.stream()
                .map(M -> MeetingSearchDto.meetingToMeetingSearchDto(M))
                .collect(Collectors.toList());

        return MeetingSearchListDto.of(meetingSearchDtos, nextPagingUrl);
    }

    @Override
    @Transactional
    public void checkJoinRequestValid(long meetingId, long memberId) {
        Meeting meeting = meetingRepository.findByIdUsingLock(meetingId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEETING_NOT_FOUND));

        if(meeting.isFull()) {
            throw new BusinessException(ErrorCode.MAX_PARTICIPANTS);
        }

        Member member = memberService.findById(memberId);

        Set<MemberMeeting> participants = meeting.getParticipants();

        boolean alreadyJoin = participants.stream().anyMatch(M -> Objects.equals(M.getMemberId(), memberId));
        if(alreadyJoin) {
            throw new BusinessException(ErrorCode.ALREADY_JOIN);
        }

        memberMeetingRepository.save(MemberMeeting.of(member, meeting));
        if(participants.size() + 1 == meeting.getMemberLimit()) {
            meeting.setFullFlag(true);
        }
    }
}
