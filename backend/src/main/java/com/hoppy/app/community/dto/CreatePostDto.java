package com.hoppy.app.community.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * packageName    : com.hoppy.app.community.dto
 * fileName       : CreatePostDto
 * author         : Kim
 * date           : 2022-08-29
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022-08-29        Kim       최초 생성
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePostDto {

    @NotBlank(message = "모임 ID 누락")
    Long meetingId;

    @NotBlank(message = "제목을 작성해주세요")
    @Length(max = 20, message = "글자 제한을 초과하였습니다")
    String title;

    @Length(max = 256, message = "글자 제한을 초과하였습니다")
    String content;

    String url;
}
