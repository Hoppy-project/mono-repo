package com.hoppy.app.login.auth.handler;

import com.hoppy.app.login.auth.authentication.CustomUserDetails;
import com.hoppy.app.login.auth.provider.AuthTokenProvider;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final AuthTokenProvider authTokenProvider;
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        SecurityContextHolder.getContext().setAuthentication(authentication);

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        String token = authTokenProvider.createUserAuthToken(userDetails.getId().toString()).getToken();

        response.setHeader("Authorization", "Bearer " + token);

        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null) ip = request.getRemoteAddr();
        log.info("_______________________________리다이렉트 요청한 ip {}", ip);

        if(StringUtils.equals(ip, ""))
            response.sendRedirect("https://hoppy.kro.kr/login/auth/kakao?token=" + token);
        else
            response.sendRedirect("http://localhost:8888/login/auth/kakao?token=" + token);
    }
}
