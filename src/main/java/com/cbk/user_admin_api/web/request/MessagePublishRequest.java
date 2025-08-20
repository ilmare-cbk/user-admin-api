package com.cbk.user_admin_api.web.request;

import com.cbk.user_admin_api.application.command.MessageCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(name = "(카카오톡)메시지 요청")
@Getter
public class MessagePublishRequest {
    @Schema(description = "연령대", example = "20")
    private int ageGroup; // 연령대

    @Schema(description = "메시지", example = "테스트 메시지입니다.")
    private String message;

    public MessageCommand toCommand() {
        return MessageCommand.of(ageGroup, message);
    }
}
