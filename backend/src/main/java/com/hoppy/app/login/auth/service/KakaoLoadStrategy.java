package com.hoppy.app.login.auth.service;

import com.hoppy.app.login.auth.SocialType;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Slf4j
//@Component
@Service
public class KakaoLoadStrategy extends SocialLoadStrategy{

    protected Map<String, Object> sendRequestToSocialSite(HttpEntity request) {
        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(SocialType.KAKAO.getUserInfoUrl(),
                    SocialType.KAKAO.getMethod(),
                    request,
                    RESPONSE_TYPE);

            LinkedHashMap properties = (LinkedHashMap) response.getBody().get("properties");
            String nickname = (String) properties.get("nickname");
            String profileImageUrl = (String) properties.get("profile_image");

            LinkedHashMap kakaoAccount = (LinkedHashMap) response.getBody().get("kakao_account");
            String email = (String) kakaoAccount.get("email");
            
            Map<String, Object> userInfoMap = new HashMap<>();
            userInfoMap.put("id", response.getBody().get("id"));
            userInfoMap.put("nickname", nickname);
            userInfoMap.put("profile_image", profileImageUrl);
            userInfoMap.put("email", email);

            System.out.println("userInfoMap = " + userInfoMap);

            return userInfoMap;

        } catch (Exception e) {
            log.error("KAKAO 사용자 정보를 받아오던 중 에러가 발생했습니다. {}", e.getMessage());
            throw e;
        }
    }
}