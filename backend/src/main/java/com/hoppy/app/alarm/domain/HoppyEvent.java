package com.hoppy.app.alarm.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * packageName    : com.hoppy.app.meeting.controller
 * fileName       : CacheEvent
 * author         : Kim
 * date           : 2022-09-27
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022-09-27        Kim       최초 생성
 */
@Getter
@Builder
@AllArgsConstructor
public class HoppyEvent {

    private EventType type;
    private String url;
    private String content;
}
