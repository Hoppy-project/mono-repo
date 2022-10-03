package com.hoppy.app.alarm.service;

import com.hoppy.app.alarm.domain.EventType;
import com.hoppy.app.alarm.domain.HoppyEvent;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * packageName    : com.hoppy.app.alarm.service
 * fileName       : AlarmService
 * author         : Kim
 * date           : 2022-10-03
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022-10-03        Kim       최초 생성
 */
public interface AlarmService {

    String createKey(long userId);
    SseEmitter createEmitter(long userId);
    HoppyEvent createEvent(String key, EventType type, String url, String content);

    void sendEvent(long userId, String eventId, HoppyEvent event);
    void sendMissingEvent(long userId, String lastEventId);

}
