package com.cbk.user_admin_api.web.request;

import com.cbk.user_admin_api.application.command.LoginCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Schema(name = "로그인 요청")
@Getter
public class LoginRequest {
    @Schema(description = "계정")
    @NotNull
    private String userId;

    @Schema(description = "암호")
    @NotNull
    private String password;

    public LoginCommand toCommand() {
        return LoginCommand.of(userId, password);
    }
}
