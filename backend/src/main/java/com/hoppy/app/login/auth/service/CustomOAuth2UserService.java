package com.hoppy.app.login.auth.service;

import com.hoppy.app.login.auth.SocialType;
import com.hoppy.app.login.auth.authentication.CustomUserDetails;
import com.hoppy.app.login.auth.authentication.KakaoOAuth2UserInfo;
import com.hoppy.app.login.auth.authentication.OAuth2UserInfo;
import com.hoppy.app.member.Role;
import com.hoppy.app.member.domain.Member;
import com.hoppy.app.like.domain.LikeManager;
import com.hoppy.app.like.repository.LikeManagerRepository;
import com.hoppy.app.member.repository.MemberRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
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

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    private final LikeManagerRepository likeManagerRepository;

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

        /**
         * (Note: ????????? Member ?????? ?????? ?????? ?????? ????????? ???)
         * ???????????? ????????? ??????, DB??? ???????????? ????????? ????????? ???????????? ????????? ?????? ???????????? Update
         * ???????????? ???????????? ?????? ??????, ????????? ????????? ????????? DB??? ??????.
         */

        if(savedMember.isPresent()) {
            updateMember(savedMember.get(), userInfo);
            return CustomUserDetails.create(savedMember.get(), user.getAttributes());
        } else {
            Member member = createMember(userInfo, socialType);
            return CustomUserDetails.create(member, user.getAttributes());
        }
    }

    private Member createMember(OAuth2UserInfo userInfo, SocialType socialType) {

//        System.out.println("CustomOAuth2UserService.createMember");

        LikeManager likeManager = LikeManager.builder().build();
        likeManager = likeManagerRepository.save(likeManager);

        Member member = Member.builder()
                .socialType(socialType)
                .id(Long.parseLong(userInfo.getSocialId()))
                .email(userInfo.getEmail())
                .profileImageUrl(userInfo.getProfileImageUrl())
                .username(userInfo.getUsername())
                .role(Role.USER)
                .deleted(false)
                .likeManager(likeManager)
                .build();

        return memberRepository.save(member);
    }

    private Member updateMember(Member member, OAuth2UserInfo userInfo) {
        /**
         * update(????????? ??????)??? ?????? ?????? ???????????? ????????????, update ?????? api??? ???????????? ??????????????? ???????????? ????????? ?????? ??????.
         */
        if (userInfo.getUsername() != null && !member.getUsername().equals(userInfo.getUsername())) {
            member.setUsername(userInfo.getUsername());
        }
        if(userInfo.getProfileImageUrl() != null && !member.getProfileImageUrl().equals(userInfo.getProfileImageUrl())) {
            member.setProfileImageUrl(userInfo.getProfileImageUrl());
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
