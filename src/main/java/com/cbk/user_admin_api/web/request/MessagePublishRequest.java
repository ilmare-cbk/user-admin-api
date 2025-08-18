package com.cbk.user_admin_api.web.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(name = "(카카오톡)메세지 요청")
@Getter
public class MessagePublishRequest {
    @Schema(description = "연련대", example = "20")
    private int ageGroup; // 연령대
}
