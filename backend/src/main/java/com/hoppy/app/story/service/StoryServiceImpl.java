package com.hoppy.app.story.service;

import com.hoppy.app.like.domain.MemberStoryLike;
import com.hoppy.app.like.repository.MemberStoryLikeRepository;
import com.hoppy.app.like.repository.MemberStoryReReplyLikeRepository;
import com.hoppy.app.like.repository.MemberStoryReplyLikeRepository;
import com.hoppy.app.member.domain.Member;
import com.hoppy.app.member.service.MemberService;
import com.hoppy.app.response.error.exception.BusinessException;
import com.hoppy.app.response.error.exception.ErrorCode;
import com.hoppy.app.story.domain.story.Story;
import com.hoppy.app.story.domain.story.StoryReply;
import com.hoppy.app.story.dto.PagingStoryDto;
import com.hoppy.app.story.dto.StoryDetailDto;
import com.hoppy.app.story.dto.SaveStoryDto;
import com.hoppy.app.story.dto.StoryDto;
import com.hoppy.app.story.dto.StoryReplyRequestDto;
import com.hoppy.app.story.dto.UploadStoryDto;
import com.hoppy.app.story.repository.StoryReReplyRepository;
import com.hoppy.app.story.repository.StoryReplyRepository;
import com.hoppy.app.story.repository.StoryRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StoryServiceImpl implements StoryService {

    private final StoryRepository storyRepository;

    private final StoryReplyRepository storyReplyRepository;

    private final StoryReReplyRepository storyReReplyRepository;

    private final MemberStoryLikeRepository memberStoryLikeRepository;

    private final MemberStoryReplyLikeRepository memberStoryReplyLikeRepository;

    private final MemberStoryReReplyLikeRepository memberStoryReReplyLikeRepository;
    private final MemberService memberService;


    @Override
    public Story findByStoryId(Long storyId) {
        Optional<Story> optStory = storyRepository.findById(storyId);
        if(optStory.isEmpty()) {
            throw new BusinessException(ErrorCode.STORY_NOT_FOUND);
        }
        return optStory.get();
    }


    @Override
    public void saveStory(Story story, Member member) {
        storyRepository.save(story);
    }

    @Override
    @Transactional
    public Story uploadStory(UploadStoryDto dto, Member member) {
        return Story.of(dto, member);
    }

    @Override
    @Transactional
    public Story updateStory(UploadStoryDto dto, Long storyId) {
        Story story = findByStoryId(storyId);
        if(dto.getTitle() != null) {
            story.setTitle(dto.getTitle());
        }
        if(dto.getContent() != null) {
            story.setContent(dto.getContent());
        }
        if(dto.getFilename() != null) {
            story.setFilePath(dto.getFilename());
        }
        return story;
    }

    @Override
    @Transactional
    public void deleteStory(Long storyId) {
        Story story = findByStoryId(storyId);
        if(!story.getLikes().isEmpty()) {
            List<Long> likeList = new ArrayList<>();
            for(var l : story.getLikes()) {
                likeList.add(l.getId());
            }
            memberStoryLikeRepository.deleteAllByList(likeList);
        }
        if(!story.getReplies().isEmpty()) {
            List<Long> replyList = new ArrayList<>();
            List<Long> reReplyList = new ArrayList<>();
            List<Long> replyLikeList = new ArrayList<>();
            List<Long> reReplyLikeList = new ArrayList<>();
            for(var r : story.getReplies()) {
                replyList.add(r.getId());
                for (var l : r.getLikes()) {
                    replyLikeList.add(l.getId());
                }
                for (var rr : r.getReReplies()) {
                    reReplyList.add(rr.getId());
                    for (var ll : rr.getLikes()) {
                        reReplyLikeList.add(ll.getId());
                    }
                }
            }
            if (!reReplyLikeList.isEmpty()) memberStoryReReplyLikeRepository.deleteAllByList(reReplyLikeList);
            storyReReplyRepository.deleteAllByList(reReplyList);
            if (!replyLikeList.isEmpty()) memberStoryReplyLikeRepository.deleteAllByList(replyLikeList);
            storyReplyRepository.deleteAllByList(replyList);
        }
        storyRepository.delete(story);
    }

    @Override
    public List<SaveStoryDto> showMyStoriesInProfile(Member member) {
        List<Story> stories = storyRepository.findByMemberIdOrderByIdDesc(member.getId());
        return stories.stream().map(story -> SaveStoryDto.of(story, member)).collect(
                Collectors.toList());
    }


    @Override
    @Transactional
    public PagingStoryDto pagingStory(Long memberId, Long lastId) {
        lastId = validCheckLastId(lastId);
        List<Story> storyList = storyRepository.findNextStoryOrderByIdDesc(lastId, PageRequest.of(0, 10));
        if(storyList.isEmpty()) {
            throw new BusinessException(ErrorCode.NO_MORE_STORY);
        }
        lastId = getLastId(storyList);
        String nextPageUrl = getNextPagingUrl(lastId);
        List<StoryDto> storyDtoList = listToDtoList(storyList);
        List<Boolean> likedList = listToLikeList(storyList, memberId);
        setStoryDtoLikes(storyDtoList, likedList);
        return PagingStoryDto.of(storyDtoList, nextPageUrl);
    }

    public void setStoryDtoLikes(List<StoryDto> dtoList, List<Boolean> likeList) {
        for (int i = 0; i < dtoList.size(); i++) {
            dtoList.get(i).setLiked(likeList.get(i));
        }
    }

    public List<StoryDto> listToDtoList(List<Story> storyList) {
        return storyList.stream().map(StoryDto::of).collect(Collectors.toList());
    }

    public List<Boolean> listToLikeList(List<Story> storyList, Long memberId) {
        List<Boolean> likeList = new ArrayList<>();
        for (int i = 0; i < storyList.size(); i++) {
            Story story = storyList.get(i);
            Optional<MemberStoryLike> memberStoryLike =
                    memberStoryLikeRepository.findByMemberIdAndStoryId(memberId, story.getId());
            if (memberStoryLike.isPresent()) {
                likeList.add(true);
            } else {
                likeList.add(false);
            }
        }
        return likeList;
    }

    public Long validCheckLastId(Long lastId) {
        return (lastId == 0) ? Long.MAX_VALUE : lastId;
    }

    public long getLastId(List<Story> storyList) {
        return storyList.get(storyList.size() - 1).getId();
    }

    public String getNextPagingUrl(Long lastId) {
        if(lastId >= 0) {
//            return "https://hoppy.kro.kr/api/story?lastId=" + lastId;
            return String.valueOf(lastId);
        } else {
            return "end";
        }
    }

    @Override
    @Transactional
    public void likeStory(Long memberId, Long storyId) {
        Optional<MemberStoryLike> likeOptional = memberStoryLikeRepository.findByMemberIdAndStoryId(memberId, storyId);

        // TODO: 이미 좋아요를 누른 상태라면 '좋아요 취소' 기능 구현 필요
        // TODO: 단, 좋아요 광클 시 데이터 처리를 고려해야함
        if (likeOptional.isPresent()) {
            return;
        }
        Member member = memberService.findById(memberId);
        Story story = findByStoryId(storyId);
        memberStoryLikeRepository.save(MemberStoryLike.of(member, story));
    }

    @Override
    @Transactional
    public void dislikeStory(Long memberId, Long storyId) {
        memberStoryLikeRepository.deleteByMemberIdAndStoryId(memberId, storyId);
    }

    @Override
    public void likeOrDislikeStory(Long memberId, Long storyId) {
        Optional <MemberStoryLike> optional = memberStoryLikeRepository.findByMemberIdAndStoryId(memberId, storyId);
        if(optional.isPresent()) {
            memberStoryLikeRepository.delete(optional.get());
        } else {
            Member member = memberService.findById(memberId);
            Story story = findByStoryId(storyId);
            memberStoryLikeRepository.save(MemberStoryLike.of(member, story));
        }
    }

    @Override
    public StoryDetailDto showStoryDetails(Long memberId, Long storyId) {
        Story story = findByStoryId(storyId);
        Optional<MemberStoryLike> optional = memberStoryLikeRepository.findByMemberIdAndStoryId(memberId, storyId);
        StoryDetailDto dto = StoryDetailDto.from(story);
        if (optional.isPresent()) dto.setLiked(true);
        else dto.setLiked(false);
        return dto;
    }
}
