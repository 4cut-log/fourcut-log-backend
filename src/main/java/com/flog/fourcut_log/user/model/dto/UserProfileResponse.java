package com.flog.fourcut_log.user.model.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserProfileResponse {
    private String nickname;
    private String email;
}
