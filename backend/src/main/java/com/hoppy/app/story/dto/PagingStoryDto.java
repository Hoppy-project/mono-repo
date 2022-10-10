package com.hoppy.app.story.dto;

import com.hoppy.app.story.domain.story.Story;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagingStoryDto {

    private List<StoryDto> storyList;
    private List<Boolean> likedList;
    private String lastId;

    public static PagingStoryDto of(List<StoryDto> storyList, List<Boolean> likedList, String lastId) {
        return PagingStoryDto.builder()
                .storyList(storyList)
                .likedList(likedList)
                .lastId(lastId)
                .build();
    }
}
