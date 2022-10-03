package com.hoppy.app.alarm.controller;

import com.hoppy.app.alarm.domain.EventType;
import com.hoppy.app.alarm.domain.HoppyEvent;
import com.hoppy.app.alarm.service.AlarmService;
import com.hoppy.app.login.auth.authentication.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * packageName    : com.hoppy.app.alarm.controller
 * fileName       : AlarmController
 * author         : Kim
 * date           : 2022-09-27
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022-09-27        Kim       최초 생성
 */
@RestController
@RequestMapping("/alarm")
@RequiredArgsConstructor
@Slf4j
public class AlarmController {

    private final AlarmService alarmService;

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId
    ) {
        long userId = userDetails.getId();
        SseEmitter emitter = alarmService.createEmitter(userId);

        String key = alarmService.createKey(userId);
        HoppyEvent event = alarmService.createEvent(key, EventType.DUMMY, null, null);
        alarmService.sendEvent(userId, key, event);

        if(isNotEmpty(lastEventId)) {
            alarmService.sendMissingEvent(userId, lastEventId);
        }

        return emitter;
    }

    @GetMapping("/test")
    public void testSendData() {

        final long userId = 1L;

        String eventId = alarmService.createKey(userId);
        HoppyEvent event = alarmService.createEvent(eventId, EventType.COMMUNITY, UUID.randomUUID().toString().substring(0, 8), "테스트입니다");

        alarmService.sendEvent(userId, eventId, event);
    }
}
