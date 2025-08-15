package com.cbk.user_admin_api.web.request;

import com.cbk.user_admin_api.application.command.UserSignupCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Schema(name = "회원가입 요청")
@Getter
public class UserSignupRequest {
    @Schema(description = "계정", example = "user01")
    @NotNull
    private String userId;

    @Schema(description = "암호", example = "secret1234")
    @NotNull
    private String password;

    @Schema(description = "성명", example = "홍길동")
    @NotNull
    private String name;

    @Schema(description = "주민등록번호(하이픈 없이)", example = "9001011234567")
    @NotNull
    private String ssn;

    @Schema(description = "핸드폰 번호(하이픈 없이)", example = "01012345678")
    @NotNull
    private String phoneNumber;

    @Schema(description = "주소", example = "서울특별시 종로구")
    @NotNull
    private String address;

    public UserSignupCommand toCommand() {
        return UserSignupCommand.of(userId, password, name, ssn, phoneNumber, address);
    }
}
