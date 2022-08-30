package com.hoppy.app.story.service;

import com.hoppy.app.member.domain.Member;
import com.hoppy.app.story.domain.story.Story;
import com.hoppy.app.story.dto.PagingStoryDto;
import com.hoppy.app.story.dto.SaveStoryDto;
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

    public PagingStoryDto pagingStory(Long lastId);

    public void likeStory(Long memberId, Long storyId);

    public void dislikeStory(Long memberId, Long storyId);

    public void uploadStoryReply(Long memberId, Long storyId, StoryReplyRequestDto dto);

    public void deleteStoryReply(Long storyId, Long replyId);

}
