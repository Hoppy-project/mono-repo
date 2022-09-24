package com.hoppy.app.response.service;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum SuccessCode {
    //    Auth
    SIGN_SUCCESS("회원가입 성공", 200),
    LOGIN_SUCCESS("로그인 성공", 200),
    LOGOUT_SUCCESS("로그아웃 성공", 200),
    REISSUE_SUCCESS("토큰 재발급 성공", 200),
    UPDATE_SUCCESS("회원 정보 수정 성공", 200),
    DELETE_SUCCESS("회원 삭제 성공", 200),

    // health check
    HEALTH_CHECK_SUCCESS("정상 동작 중", 200),
    PROFILE_CHECK_SUCCESS("프로파일 조회 완료", 200),

    // meeting
    GET_PRESIGNED_URL_SUCCESS("URL 발급 완료", 200),

    CREATE_MEETING_SUCCESS("모임 생성 완료", 200),
    UPDATE_MEETING_SUCCESS("모임 수정 완료", 200),
    INQUIRY_MEETING_SUCCESS("모임 조회 완료", 200),
    INQUIRE_MEETING_DETAIL_SUCCESS("모임 상세 페이지 조회 완료", 200),
    JOIN_MEETING_SUCCESS("모임 참가 완료", 200),
    WITHDRAW_MEETING_SUCCESS("모임 탈퇴 완료", 200),
    INQUIRY_COMMUNITY_POSTS_SUCCESS("커뮤니티 게시물 조회 완료", 200),
    SEARCH_MEETING_SUCCESS("모임 검색 완료", 200),

    // post
    GET_POST_DETAIL_SUCCESS("게시물 조회 완료", 200),
    CREATE_POST_SUCCESS("게시물 생성 완료", 200),
    DELETE_POST_SUCCESS("게시물 제거 완료", 200),
    PATCH_POST_SUCCESS("게시물 수정 완료", 200),

    // member
    SHOW_PROFILE_SUCCESS("유저 프로필 조회 완료", 200),

    // Story
    UPLOAD_STORY_SUCCESS("스토리 업로드 완료", 200),
    DELETE_STORY_SUCCESS("스토리 삭제 완료", 200),

    UPDATE_STORY_SUCCESS("스토리 수정 완료", 200),
    INQUIRY_STORY_SUCCESS("스토리 조회 완료", 200),

    // Reply
    DELETE_REPLY_SUCCESS("댓글 삭제 완료", 200),
    ;

    private final String message;
    private final int status;
}