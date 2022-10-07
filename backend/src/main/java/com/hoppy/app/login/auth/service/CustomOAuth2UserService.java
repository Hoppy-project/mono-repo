package com.hoppy.app.login.auth.service;

import com.hoppy.app.login.auth.SocialType;
import com.hoppy.app.login.auth.authentication.CustomUserDetails;
import com.hoppy.app.login.auth.authentication.KakaoOAuth2UserInfo;
import com.hoppy.app.login.auth.authentication.KakaoOAuth2UserInfoDto;
import com.hoppy.app.login.auth.authentication.OAuth2UserInfo;
import com.hoppy.app.member.Role;
import com.hoppy.app.member.domain.Member;
import com.hoppy.app.member.repository.MemberRepository;
import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User user = super.loadUser(userRequest);

        try {
            return this.process(userRequest, user);
        } catch (AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalAuthenticationServiceException(e.getMessage(), e.getCause());
        }
    }

    private OAuth2User process(OAuth2UserRequest userRequest, OAuth2User user) {

        SocialType socialType = SocialType.KAKAO;

        OAuth2UserInfo userInfo = new KakaoOAuth2UserInfo(user.getAttributes());
        Optional<Member> savedMember = memberRepository.findById(Long.valueOf(userInfo.getSocialId()));

        Member member;
        if (savedMember.isPresent()) {
            /**
             * 존재하는 멤버일 경우, 탈퇴여부와 이메일 변경 여부를 확인하고 update 후, db에 저장
             */
            member = updateMember(savedMember.get(), userInfo);
            memberRepository.save(member);
        } else {
            member = createMember(userInfo, socialType);
        }
        return CustomUserDetails.create(member, user.getAttributes());
    }

    @Transactional
    public Member loginProcess(KakaoOAuth2UserInfoDto dto) {

        final SocialType socialType = SocialType.KAKAO;

        Member member = memberRepository.findById(dto.getSocialId())
                .orElseGet(() -> createMember(dto, socialType));

        return updateMember(member, dto);
    }

    private String createUsername(String username) {
        int len = username.length();
        if(len < 1) {
            len = 4;
        } else if(len >= 1 && len <= 3) {
            len = len + 2;
        } else {
            len = 5;
        }
        RandomStringService stringService = new RandomStringService(len);
        return stringService.nextString();
    }

    private Member createMember(OAuth2UserInfo userInfo, SocialType socialType) {
        return memberRepository.save(
                Member.builder()
                        .socialType(socialType)
                        .id(Long.parseLong(userInfo.getSocialId()))
                        .email(userInfo.getEmail())
                        .profileImageUrl(userInfo.getProfileImageUrl())
                        .username(createUsername(userInfo.getUsername()))
                        .role(Role.USER)
                        .deleted(false)
                        .build()
        );
    }

    private Member createMember(KakaoOAuth2UserInfoDto dto, SocialType socialType) {
        return memberRepository.save(
                Member.builder()
                        .socialType(socialType)
                        .id(dto.getSocialId())
                        .email(dto.getEmail())
                        .profileImageUrl(dto.getProfileUrl())
                        .username(createUsername("해피_" + UUID.randomUUID().toString().substring(0, 5)))
                        .role(Role.USER)
                        .deleted(false)
                        .build()
        );
    }

    private Member updateMember(Member member, OAuth2UserInfo userInfo) {
        /**
         * 기존 존재하는 멤버의 이메일이 달라졌을 경우에만 수정 진행.
         * 실제 마이 프로필 수정은 update API에서 담당
         */
        if(userInfo.getEmail() != null && !member.getEmail().equals(userInfo.getEmail())) {
            member.setEmail(userInfo.getEmail());
        }
        /**
         * 탈퇴한 회원일 경우, 재가입 처리
         */
        if(member.isDeleted()) {
            member.setDeleted(false);
        }
        return member;
    }

    @Transactional
    public Member updateMember(Member member, KakaoOAuth2UserInfoDto dto) {
        /**
         * 기존 존재하는 멤버의 이메일이 달라졌을 경우에만 수정 진행.
         * 실제 마이 프로필 수정은 update API에서 담당
         */
        if(isNotEmpty(dto.getEmail()) && !StringUtils.equals(member.getEmail(), dto.getEmail())) {
            member.setEmail(dto.getEmail());
        }
        /**
         * 탈퇴한 회원일 경우, 재가입 처리
         */
        if(member.isDeleted()) {
            member.setDeleted(false);
        }
        return member;
    }

    @PreAuthorize("isAuthenticated()")
    public Long saveMember(Member member) {
        return memberRepository.save(member).getId();
    }

    @PreAuthorize("isAuthenticated()")
    public void print() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        principal.printName();
    }
}
