package com.cbk.user_admin_api.web.controller;

import com.cbk.user_admin_api.application.query.UserDetail;
import com.cbk.user_admin_api.application.service.UserQueryService;
import com.cbk.user_admin_api.web.response.UserDetailResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
@SecurityRequirement(name = "JWT")
@Tag(name = "회원 API", description = "회원 관련 API")
public class UserRestController {
    private final UserQueryService userQueryService;

    @GetMapping("/me")
    ResponseEntity<UserDetailResponse> readMe(@AuthenticationPrincipal UserDetails userDetails) {
        UserDetail user = userQueryService.readUser(userDetails.getUsername());
        return ResponseEntity.ok(UserDetailResponse.of(user.getUserId(),
                                                       user.getName(),
                                                       user.getSsn(),
                                                       user.getPhoneNumber(),
                                                       user.getAddress()));
    }
}
