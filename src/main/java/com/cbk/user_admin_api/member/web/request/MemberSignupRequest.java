package com.cbk.user_admin_api.member.web.request;

import com.cbk.user_admin_api.member.application.command.MemberSignupCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Schema(name = "회원가입 요청")
@Getter
public class MemberSignupRequest {
    @Schema(description = "계정")
    @NotNull
    private String memberId;

    @Schema(description = "암호")
    @NotNull
    private String password;

    @Schema(description = "성명")
    @NotNull
    private String name;

    @Schema(description = "주민등록번호(하이픈 없이)")
    @NotNull
    private String ssn;

    @Schema(description = "핸드폰 번호(하이픈 없이)")
    @NotNull
    private String phoneNumber;

    @Schema(description = "주소")
    @NotNull
    private String address;

    public MemberSignupCommand toCommand() {
        return MemberSignupCommand.of(memberId, password, name, ssn, phoneNumber, address);
    }
}
