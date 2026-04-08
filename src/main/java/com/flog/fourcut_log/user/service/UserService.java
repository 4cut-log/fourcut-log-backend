package com.flog.fourcut_log.user.service;

import com.flog.fourcut_log.user.entity.User;
import com.flog.fourcut_log.user.model.dto.UserProfileResponse;
import com.flog.fourcut_log.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final RedisTemplate<String, String> redisTemplate;

    /**
     * OAuth2 로그인시 사용자 저장 또는 업데이트
     */
    public User saveOrUpdateOAuth2User(User newUser) {
        User user = userRepository.findByEmail(newUser.getEmail())
                .map(existingUser -> existingUser.update(newUser.getNickname()))
                .orElse(newUser);

        return userRepository.save(user);
    }

    /**
     * 이메일로 사용자 찾기
     */
    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일의 사용자를 찾을 수 없습니다: " + email));
    }

    /**
     * 내 프로필 조회
     */
    @Transactional(readOnly = true)
    public UserProfileResponse getProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        return UserProfileResponse.builder()
                .nickname(user.getNickname())
                .email(user.getEmail())
                .build();
    }

    /**
     * 닉네임 수정
     */
    public void updateNickname(Long userId, String nickname) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        user.update(nickname);
    }

    /**
     * 회원 탈퇴 (연관 데이터 포함 삭제)
     */
    public void deleteAccount(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        redisTemplate.delete(user.getEmail());
        userRepository.deleteFlogTagsByUserId(userId);
        userRepository.deleteFlogsByUserId(userId);
        userRepository.deleteTagsByUserId(userId);
        userRepository.deleteById(userId);
    }
}
