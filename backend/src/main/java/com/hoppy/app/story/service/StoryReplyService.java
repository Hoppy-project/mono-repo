package com.hoppy.app.story.service;

import com.hoppy.app.story.domain.story.StoryReReply;
import com.hoppy.app.story.domain.story.StoryReply;
import com.hoppy.app.story.dto.StoryReReplyDto;
import com.hoppy.app.story.dto.StoryReReplyRequestDto;
import com.hoppy.app.story.dto.StoryReplyRequestDto;
import com.hoppy.app.story.dto.UpdateStoryReReplyDto;
import com.hoppy.app.story.dto.UpdateStoryReplyDto;

// TODO: 스토리에 달린 댓글이 많을 경우, 무한 스크롤을 고려해야 함.
public interface StoryReplyService {

    public StoryReply checkStoryReReplies(Long memberId, Long replyId);
    public StoryReply findByReplyId(Long replyId);

    public StoryReReply findByReReplyId(Long reReplyId);

    public StoryReply uploadStoryReply(Long memberId, Long storyId, StoryReplyRequestDto dto);

    public void deleteStoryReply(Long memberId, Long replyId);

    public void updateStoryReply(Long memberId, Long replyId, UpdateStoryReplyDto dto);

    public void likeOrDislikeStoryReply(Long memberId, Long replyId);

    // TODO: 대댓글 등록, 대댓글 삭제, 대댓글 좋아요 기능
    public StoryReReply uploadStoryReReply(Long memberId, Long replyId, StoryReReplyRequestDto dto);
    public void deleteStoryReReply(Long memberId, Long reReplyId);
    public void updateStoryReReply(Long memberId, Long reReplyId, UpdateStoryReReplyDto dto);

    public void likeOrDisLikeStoryReReply(Long memberId, Long reReplyId);
}
