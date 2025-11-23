package com.multicampus.gamesungcoding.a11ymarketserver.feature.user.service;

import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.DataNotFoundException;
import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.UserNotFoundException;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.model.UserA11ySettings;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.model.UserA11yUpdateRequest;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.repository.UserA11ySettingsRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserA11ySettingsService {

    private final UserA11ySettingsRepository userA11ySettingsRepository;
    private final UserRepository userRepository;

    // 내 접근성 설정 조회
    public UserA11ySettings getMySettings(String userEmail) {

        UUID userId = getUserIdByEmail(userEmail);

        return userA11ySettingsRepository.findByUserId(userId)
                .orElseThrow(() -> new DataNotFoundException("Accessibility settings not found"));
    }

    // 접근성 설정 저장 / 수정
    @Transactional
    public void updateMySettings(String userEmail, UserA11yUpdateRequest dto) {

        UUID userId = getUserIdByEmail(userEmail);

        UserA11ySettings settings =
                userA11ySettingsRepository.findByUserId(userId)
                        .orElse(UserA11ySettings.builder()
                                .userId(userId)
                                .contrastLevel(0)
                                .textSizeLevel(0)
                                .textSpacingLevel(0)
                                .lineHeightLevel(0)
                                .textAlign("left")
                                .screenReader(0)
                                .smartContrast(0)
                                .highlightLinks(0)
                                .cursorHighlight(0)
                                .build());


        settings.updateSettings(
                dto.contrastLevel(),
                dto.textSizeLevel(),
                dto.textSpacingLevel(),
                dto.lineHeightLevel(),
                dto.textAlign(),
                booleanToInt(dto.screenReader()),
                booleanToInt(dto.smartContrast()),
                booleanToInt(dto.highlightLinks()),
                booleanToInt(dto.cursorHighlight())
        );

        userA11ySettingsRepository.save(settings);

    }


    private UUID getUserIdByEmail(String userEmail) {
        return userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found"))
                .getUserId();
    }

    private Integer booleanToInt(Boolean value) {
        return value != null && value ? 1 : 0;
    }
}
