package com.hoppy.app.meeting.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateMeetingDto {

    @NotNull(message = "카테고리를 선택해주세요")
    int category;

    @NotBlank(message = "제목을 작성해주세요")
    @Length(max = 20, message = "글자 제한을 초과하였습니다")
    String title;

    @NotBlank(message = "모집 글을 작성해주세요")
    @Length(max = 256, message = "글자 제한을 초과하였습니다")
    String content;

    @NotNull(message = "정원을 입력해주세요")
    @Min(value = 2, message = "최소 인원은 2명 입니다")
    @Max(value = 20, message = "최대 인원은 20명 입니다")
    int memberLimit;

    String url;
}