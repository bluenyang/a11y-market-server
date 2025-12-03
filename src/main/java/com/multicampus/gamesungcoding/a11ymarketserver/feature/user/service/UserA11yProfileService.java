package com.multicampus.gamesungcoding.a11ymarketserver.feature.user.service;

import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.DataNotFoundException;
import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.InvalidRequestException;
import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.UserNotFoundException;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.dto.UserA11yProfileReq;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.dto.UserA11yProfileResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.entity.*;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.repository.UserA11yProfileRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserA11yProfileService {

    private final UserA11yProfileRepository profileRepository;
    private final UserRepository userRepository;

    // 프로필 목록 조회
    public List<UserA11yProfileResponse> getMyProfiles(String userEmail) {
        Users user = getUserByEmail(userEmail);
        var list = profileRepository.findAllByUserOrderByCreatedAtAsc(user);
        return list.stream()
                .map(UserA11yProfileResponse::fromEntity)
                .toList();
    }


    // 프로필 생성
    @Transactional
    public UserA11yProfileResponse createProfile(String userEmail, UserA11yProfileReq dto) {
        Users user = getUserByEmail(userEmail);

        // 프로필 이름 중복 체크
        if (profileRepository.existsByUserAndProfileInfo_ProfileName(user, dto.profileName())) {
            throw new InvalidRequestException("이미 존재하는 프로필 이름입니다.");
        }

        UserA11yProfile profile = UserA11yProfile.builder()
                .user(user)
                .profileInfo(
                        A11yProfileInfo.builder()
                                .profileName(dto.profileName())
                                .description(dto.description())
                                .contrastLevel(dto.contrastLevel())
                                .textSizeLevel(dto.textSizeLevel())
                                .textSpacingLevel(dto.textSpacingLevel())
                                .lineHeightLevel(dto.lineHeightLevel())
                                .textAlign(dto.textAlign())
                                .screenReader(dto.screenReader())
                                .smartContrast(dto.smartContrast())
                                .highlightLinks(dto.highlightLinks())
                                .cursorHighlight(dto.cursorHighlight())
                                .build()
                )
                .build();

        return UserA11yProfileResponse.fromEntity(profileRepository.save(profile));
    }

    // 프로필 수정
    @Transactional
    public void updateProfile(String userEmail, UUID profileId, UserA11yProfileReq dto) {
        Users user = getUserByEmail(userEmail);

        UserA11yProfile profile = profileRepository
                .findByProfileIdAndUser(profileId, user)
                .orElseThrow(() -> new DataNotFoundException("해당 접근성 프로필을 찾을 수 없습니다."));

        // 프로필 이름 중복 체크
        if (!profile.getProfileInfo().getProfileName().equals(dto.profileName())
                && profileRepository.existsByUserAndProfileInfo_ProfileName(user, dto.profileName())) {
            throw new InvalidRequestException("이미 존재하는 프로필 이름입니다.");
        }

        var info = A11yProfileInfo.builder()
                .profileName(dto.profileName())
                .description(dto.description())
                .contrastLevel(dto.contrastLevel())
                .textSizeLevel(dto.textSizeLevel())
                .textSpacingLevel(dto.textSpacingLevel())
                .lineHeightLevel(dto.lineHeightLevel())
                .textAlign(dto.textAlign())
                .screenReader(dto.screenReader())
                .smartContrast(dto.smartContrast())
                .highlightLinks((dto.highlightLinks()))
                .cursorHighlight((dto.cursorHighlight()))
                .build();

        profile.update(info);
    }

    // 프로필 삭제
    @Transactional
    public void deleteProfile(String userEmail, UUID profileId) {
        Users user = getUserByEmail(userEmail);

        Long deleted = profileRepository.deleteByProfileIdAndUser(profileId, user);

        if (deleted == 0) {
            throw new DataNotFoundException("삭제할 수 있는 프로필이 없습니다.");
        }
    }

    // UserEmail -> UserId 변환
    private Users getUserByEmail(String email) {
        return userRepository.findByUserEmail(email)
                .orElseThrow(() -> new UserNotFoundException("해당 사용자를 찾을 수 없습니다."));
    }

    private Integer booleanToInt(Boolean value) {
        return (value != null && value) ? 1 : 0;
    }
}
