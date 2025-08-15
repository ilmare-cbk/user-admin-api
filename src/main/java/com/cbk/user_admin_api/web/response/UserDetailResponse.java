package com.cbk.user_admin_api.web.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;

@AllArgsConstructor(staticName = "of")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Schema(name = "회원 상세 응답")
public class UserDetailResponse {
    @Schema(description = "계정")
    private String userId;

    @Schema(description = "성명")
    private String name;

    @Schema(description = "주민등록번호")
    private String ssn;

    @Schema(description = "핸드폰 번호")
    private String phoneNumber;

    @Schema(description = "주소 (최상위 행정구역 단위만 조회)")
    private String address;
}
