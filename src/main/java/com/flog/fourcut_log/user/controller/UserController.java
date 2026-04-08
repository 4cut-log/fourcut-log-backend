package com.flog.fourcut_log.user.controller;

import com.flog.fourcut_log.global.model.ApiResponse;
import com.flog.fourcut_log.global.model.ResponseCode;
import com.flog.fourcut_log.user.model.dto.UpdateNicknameRequest;
import com.flog.fourcut_log.user.model.dto.UserProfileResponse;
import com.flog.fourcut_log.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getProfile(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return ResponseEntity.ok(ApiResponse.success(ResponseCode.OK, userService.getProfile(userId)));
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<Void>> updateNickname(
            @RequestBody UpdateNicknameRequest request,
            Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        userService.updateNickname(userId, request.getNickname());
        return ResponseEntity.ok(ApiResponse.success(ResponseCode.OK, null));
    }

    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse<Void>> deleteAccount(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        userService.deleteAccount(userId);
        return ResponseEntity.ok(ApiResponse.success(ResponseCode.OK, null));
    }
}
