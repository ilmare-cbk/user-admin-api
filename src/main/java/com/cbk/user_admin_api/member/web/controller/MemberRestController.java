package com.cbk.user_admin_api.member.web.controller;

import com.cbk.user_admin_api.member.application.service.MemberSignupService;
import com.cbk.user_admin_api.member.web.request.MemberSignupRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
@Tag(name = "Member API", description = "사용자 관련 API")
public class MemberRestController {
    private final MemberSignupService memberSignupService;

    @PostMapping
    @Operation(summary = "회원가입")
    public void signup(@RequestBody @Valid MemberSignupRequest request) {
        memberSignupService.signup(request.toCommand());
    }
}
