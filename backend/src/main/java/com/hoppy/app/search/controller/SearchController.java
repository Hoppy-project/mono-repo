package com.hoppy.app.search.controller;

import com.hoppy.app.meeting.service.MeetingService;
import com.hoppy.app.response.dto.ResponseDto;
import com.hoppy.app.response.service.ResponseService;
import com.hoppy.app.response.service.SuccessCode;
import com.hoppy.app.search.dto.MeetingSearchListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

/**
 * packageName    : com.hoppy.app.search
 * fileName       : SearchController
 * author         : Kim
 * date           : 2022-09-16
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022-09-16        Kim       최초 생성
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {

    private final MeetingService meetingService;

    private final ResponseService responseService;

    @GetMapping("/meeting/{keyword}")
    public ResponseEntity<ResponseDto> searchMeeting(
            @RequestParam(value = "lastId", defaultValue = "0") long lastId,
            @PathVariable("keyword") String keyword
    ) {
        return responseService.successResult(
                SuccessCode.SEARCH_MEETING_SUCCESS,
                meetingService.searchMeetings(keyword, lastId)
        );
    }

}
