package com.cbk.user_admin_api.web.request;

import com.cbk.user_admin_api.application.command.UserUpdateCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(name = "회원수정 요청")
@Getter
public class UserUpdateRequest {
    @Schema(description = "암호")
    private String password;

    @Schema(description = "주소")
    private String address;

    public UserUpdateCommand toCommand(String userId) {
        return UserUpdateCommand.of(userId, password, address);
    }
}
