package com.cbk.user_admin_api.web.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;

@AllArgsConstructor(staticName = "from")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Schema(name = "로그인 응답")
public class LoginResponse {
    @Schema(description = "생성된 토큰")
    private String token;
}
