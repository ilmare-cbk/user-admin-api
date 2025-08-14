package com.cbk.user_admin_api.web.controller;

import com.cbk.user_admin_api.application.service.UserCommandService;
import com.cbk.user_admin_api.web.request.UserSignupRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Tag(name = "인증 API", description = "인증 관련 API")
public class AuthRestController {
    private final UserCommandService userCommandService;

    @PostMapping("/signup")
    @Operation(summary = "회원가입")
    public void signup(@RequestBody @Valid UserSignupRequest request) {
        userCommandService.signup(request.toCommand());
    }
}
