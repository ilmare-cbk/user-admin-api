package com.cbk.user_admin_api.web.controller;

import com.cbk.user_admin_api.application.query.UserPagination;
import com.cbk.user_admin_api.application.query.UserPaginationQuery;
import com.cbk.user_admin_api.application.service.UserQueryService;
import com.cbk.user_admin_api.web.response.UserPaginationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("admin")
@RequiredArgsConstructor
@Tag(name = "시스템 관리자 API", description = "회원 관리 API")
public class AdminRestController {
    private final UserQueryService userQueryService;

    @GetMapping("/users")
    @Operation(summary = "회원 목록 조회")
    public ResponseEntity<List<UserPaginationResponse>> readUsers(@RequestParam(defaultValue = "1") int page,
                                                                  @RequestParam(defaultValue = "10") int size) {

        List<UserPagination> users = userQueryService.readUsers(UserPaginationQuery.of(page, size));
        return ResponseEntity.ok(
                users.stream()
                        .map(it -> UserPaginationResponse.of(it.getUserId(),
                                                             it.getName(),
                                                             it.getSsn(),
                                                             it.getPhoneNumber(),
                                                             it.getAddress()))
                        .toList()
        );
    }
}
