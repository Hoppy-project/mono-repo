package com.hoppy.app.story.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStoryReplyDto {

    @Length(max = 256, message = "글자 제한을 초과하였습니다.")
    String content;
}
