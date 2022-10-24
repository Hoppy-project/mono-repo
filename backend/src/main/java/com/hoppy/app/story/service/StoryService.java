package com.hoppy.app.story.service;

import com.hoppy.app.member.domain.Member;
import com.hoppy.app.story.domain.story.Story;
import com.hoppy.app.story.dto.PagingStoryDto;
import com.hoppy.app.story.dto.SaveStoryDto;
import com.hoppy.app.story.dto.StoryDetailDto;
import com.hoppy.app.story.dto.StoryDto;
import com.hoppy.app.story.dto.StoryReplyRequestDto;
import com.hoppy.app.story.dto.UploadStoryDto;
import java.util.List;

public interface StoryService {

    public Story findByStoryId(Long storyId);
    public void saveStory(Story story, Member member);

    public Story uploadStory(UploadStoryDto dto, Member member);

    public Story updateStory(UploadStoryDto dto, Long storyId);

    public void deleteStory(Long storyId);

    public List<SaveStoryDto> showMyStoriesInProfile(Member member);

    public PagingStoryDto pagingStory(Long memberId, Long lastId);
    public void likeStory(Long memberId, Long storyId);

    public void dislikeStory(Long memberId, Long storyId);

    public void likeOrDislikeStory(Long memberId, Long storyId);

    public StoryDetailDto showStoryDetails(Long memberId, Long storyId);
    // TODO: 스토리 상세 정보 조회 (스토리 클릭 시)

    public void listToLikeList(List<StoryDto> storyDtoList, Long memberId);

    public List<StoryDto> listToDtoList(List<Story> storyList);
}
