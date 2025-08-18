package com.cbk.user_admin_api.web.controller;

import com.cbk.user_admin_api.application.query.UserPagination;
import com.cbk.user_admin_api.application.query.UserPaginationQuery;
import com.cbk.user_admin_api.application.service.MessagePublishService;
import com.cbk.user_admin_api.application.service.UserCommandService;
import com.cbk.user_admin_api.application.service.UserQueryService;
import com.cbk.user_admin_api.web.request.MessagePublishRequest;
import com.cbk.user_admin_api.web.request.UserUpdateRequest;
import com.cbk.user_admin_api.web.response.UserPaginationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("admin")
@RequiredArgsConstructor
@Tag(name = "시스템 관리자 API", description = "회원 관리 API")
public class AdminRestController {
    private final UserQueryService userQueryService;
    private final UserCommandService userCommandService;
    private final MessagePublishService messagePublishService;

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

    @PutMapping("/users/{userId}")
    @Operation(summary = "회원 수정")
    public void updateUser(@PathVariable String userId, @RequestBody @Valid UserUpdateRequest request) {
        userCommandService.update(request.toCommand(userId));
    }

    @DeleteMapping("/users/{userId}")
    @Operation(summary = "회원 삭제")
    public void deleteUser(@PathVariable String userId) {
        userCommandService.delete(userId);
    }

    @PostMapping("/messages")
    @Operation(summary = "연령별 메세지 전송")
    public void publishMessage(@RequestBody MessagePublishRequest request) {
        messagePublishService.publishMessage(request.getAgeGroup());
    }
}
