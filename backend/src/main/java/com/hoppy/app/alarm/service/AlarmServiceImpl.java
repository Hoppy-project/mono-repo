package com.hoppy.app.alarm.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoppy.app.alarm.domain.EventType;
import com.hoppy.app.alarm.domain.HoppyEvent;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * packageName    : com.hoppy.app.alarm.service
 * fileName       : AlarmServiceImpl
 * author         : Kim
 * date           : 2022-10-03
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022-10-03        Kim       최초 생성
 */
@Service
public class AlarmServiceImpl implements AlarmService {

    private final Map<String, SseEmitter> sseEmitterRepository = new ConcurrentHashMap<>();
    private final Map<String, HoppyEvent> eventRepository = new ConcurrentHashMap<>();
    private final long DEFAULT_TIMEOUT = 60 * 1000 * 10L; // 10분

    @Override
    public String createKey(long userId) {

        StringBuilder builder = new StringBuilder(30);
        return builder.append(userId)
                .append('-')
                .append(System.currentTimeMillis())
                .toString();
    }

    @Override
    public SseEmitter createEmitter(long userId) {

        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);

        String key = createKey(userId);
        sseEmitterRepository.put(key, emitter);

        emitter.onCompletion(() ->
                sseEmitterRepository.remove(key)
        );
        emitter.onTimeout(() -> {
            emitter.complete();
            sseEmitterRepository.remove(key);
        });
        emitter.onError((e) -> {
            emitter.completeWithError(e);
            sseEmitterRepository.remove(key);
        });

        return emitter;
    }

    @Override
    public HoppyEvent createEvent(String key, EventType type, String url, String content) {

        HoppyEvent event = HoppyEvent.builder()
                .type(type)
                .url(url)
                .content(content)
                .build();
        eventRepository.put(key, event);
        return event;
    }

    @Override
    public void sendEvent(long userId, String eventId, HoppyEvent event) {

        ObjectMapper mapper = new ObjectMapper();

        sseEmitterRepository.entrySet().removeIf(E -> {
            try {
                if(E.getKey().startsWith(userId + "-")) {
                    E.getValue().send(
                            SseEmitter.event()
                                    .id(eventId)
                                    .data(mapper.writeValueAsString(event), MediaType.TEXT_EVENT_STREAM)
                    );
                }
                return false;
            } catch (Exception e) {
                E.getValue().completeWithError(e);
                return true;
            }
        });
    }

    @Override
    public void sendMissingEvent(long userId, String lastEventId) {

        eventRepository.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(userId + "-"))
                .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                .forEach(entry -> sendEvent(userId, entry.getKey(), entry.getValue()));
    }
}
