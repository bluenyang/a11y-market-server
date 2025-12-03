package com.multicampus.gamesungcoding.a11ymarketserver.common.oauth;

import com.multicampus.gamesungcoding.a11ymarketserver.common.jwt.provider.JwtTokenProvider;
import com.multicampus.gamesungcoding.a11ymarketserver.common.properties.OAuth2Properties;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.entity.UserOauthLinks;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.entity.Users;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.repository.UserOauthLinksRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final UserOauthLinksRepository userOauthLinksRepository;
    private final OAuth2Properties oAuth2Properties;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        Map<String, Object> attributes = oAuth2User.getAttributes();
        @SuppressWarnings("unchecked") Map<String, Object> kakaoAccount =
                (Map<String, Object>) attributes.get("kakao_account");
        @SuppressWarnings("unchecked") Map<String, Object> profile =
                (Map<String, Object>) kakaoAccount.get("profile");

        Long kakaoId = (Long) attributes.get("id");
        String nickname = (String) profile.get("nickname");

        var oauthLink = userOauthLinksRepository.findByOauthProviderId(String.valueOf(kakaoId))
                .orElseGet(() -> {
                    var user = userRepository.save(
                            Users.builder()
                                    .userNickname(nickname)
                                    .build());

                    return userOauthLinksRepository.save(
                            UserOauthLinks.builder()
                                    .user(user)
                                    .oauthProvider("KAKAO")
                                    .oauthProviderId(String.valueOf(kakaoId))
                                    .build());
                });

        var user = oauthLink.getUser();
        String targetUrl;

        if (user.getUserName() == null || user.getUserPhone() == null) {
            String tempJwt = jwtTokenProvider.createTemporaryAccessToken(user.getUserId());
            targetUrl = UriComponentsBuilder.fromUriString(oAuth2Properties.getSignupUri())
                    .queryParam("temp_token", tempJwt)
                    .build()
                    .encode(StandardCharsets.UTF_8)
                    .toUriString();
        } else {
            String jwt = jwtTokenProvider.createAccessToken(user.getUserId(), user.getUserRole());
            targetUrl = UriComponentsBuilder.fromUriString(oAuth2Properties.getRedirectUri())
                    .queryParam("token", jwt)
                    .build()
                    .encode(StandardCharsets.UTF_8)
                    .toUriString();
        }

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
