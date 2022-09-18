package com.hoppy.app.meeting.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * packageName    : com.hoppy.app.meeting.dto
 * fileName       : UpdateMeetingDto
 * author         : Kim
 * date           : 2022-09-11
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022-09-11        Kim       최초 생성
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMeetingDto {

    @Length(max = 20, message = "글자 제한을 초과하였습니다")
    String title;

    @Length(max = 256, message = "글자 제한을 초과하였습니다")
    String content;

    String url;
}
