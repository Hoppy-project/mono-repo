package com.hoppy.app.member.controller;

import com.hoppy.app.login.auth.authentication.CustomUserDetails;
import com.hoppy.app.member.domain.Member;
import com.hoppy.app.member.dto.UpdateMemberDto;
import com.hoppy.app.member.dto.MyProfileDto;
import com.hoppy.app.member.service.MemberServiceImpl;
import com.hoppy.app.response.dto.ResponseDto;
import com.hoppy.app.response.error.exception.BusinessException;
import com.hoppy.app.response.error.exception.ErrorCode;
import com.hoppy.app.response.service.ResponseService;
import com.hoppy.app.response.service.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberDaoController {

    private final MemberServiceImpl memberService;
    private final ResponseService responseService;

    @PostMapping("/update")
    public ResponseEntity<ResponseDto> updateUser(
            @AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody UpdateMemberDto memberDto) {
        Member member = memberService.updateMemberById(userDetails.getId(), memberDto);
        if(member == null) {
            throw new BusinessException(ErrorCode.MEMBER_NOT_FOUND);
        }
        MyProfileDto myProfileDto = MyProfileDto.of(member);
        return responseService.successResult(SuccessCode.UPDATE_SUCCESS, myProfileDto);
    }

    /**
     * 회원 탈퇴 로직은 추후 회원의 재가입을 고려해 DB table 'deleted' 부분을 true 로 변경함으로써
     * 탈퇴한 회원을 식별
     */
    @GetMapping("/delete")
    public ResponseEntity<ResponseDto> deleteUser(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long memberId = customUserDetails.getId();
        memberService.deleteMemberById(memberId);
        return responseService.successResult(SuccessCode.DELETE_SUCCESS);
    }
}
