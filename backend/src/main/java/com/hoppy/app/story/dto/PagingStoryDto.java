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
    private String lastId;

    public static PagingStoryDto of(List<StoryDto> storyList, String lastId) {
        return PagingStoryDto.builder()
                .storyList(storyList)
                .lastId(lastId)
                .build();
    }
}
