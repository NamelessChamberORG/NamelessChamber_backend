package org.example.namelesschamber.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.namelesschamber.common.response.ApiResponse;
import org.example.namelesschamber.common.security.SecurityUtils;
import org.example.namelesschamber.domain.user.dto.request.NicknameRequestDto;
import org.example.namelesschamber.domain.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "닉네임 설정",
            description = "로그인 이후 최초로 닉네임을 설정합니다."
    )
    @PostMapping("/nickname")
    public ResponseEntity<ApiResponse<Void>> updateNickname(
            @RequestBody @Valid NicknameRequestDto request) {

        String userId = SecurityUtils.getCurrentSubject();
        userService.updateNickname(userId, request.nickname());

        return ApiResponse.success(HttpStatus.OK);
    }
}
