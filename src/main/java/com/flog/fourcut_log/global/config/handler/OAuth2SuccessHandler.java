package com.flog.fourcut_log.global.config.handler;

import com.flog.fourcut_log.auth.model.dto.OAuth2CustomUser;
import com.flog.fourcut_log.global.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;

    private static final String REDIRECT_URI = "fourcutlog://callback";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2CustomUser oAuth2User = (OAuth2CustomUser) authentication.getPrincipal();

        String email = oAuth2User.getEmail();
        String role = oAuth2User.getRole();
        Long userId = oAuth2User.getUserId();
        String nickname = URLEncoder.encode(oAuth2User.getNickname(), StandardCharsets.UTF_8);

        // JWT 발급 (Refresh Token은 JwtUtil 내부에서 Redis에 자동 저장)
        String accessToken = jwtUtil.createAccessToken(email, role, userId);
        String refreshToken = jwtUtil.createRefreshToken(email, role, userId);

        // 딥링크(또는 프론트 URL)로 토큰 담아서 리다이렉트
        String targetUrl = UriComponentsBuilder.fromUriString(REDIRECT_URI)
                .queryParam("accessToken", accessToken)
                .queryParam("refreshToken", refreshToken)
                .queryParam("nickname", nickname)
                .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
