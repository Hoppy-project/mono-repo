package com.hoppy.app.community.controller;

import com.hoppy.app.community.domain.Post;
import com.hoppy.app.community.service.PostService;
import com.hoppy.app.login.auth.authentication.CustomUserDetails;
import com.hoppy.app.response.dto.ResponseDto;
import com.hoppy.app.response.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 태경 2022-08-09
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final ResponseService responseService;
    private final PostService postService;

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> getPostDetail(
            @PathVariable("id") long id,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        // TODO: API 작성 및 테스트 코드 작성 필요
        Post post = postService.findById(id);
        return null;
    }
}