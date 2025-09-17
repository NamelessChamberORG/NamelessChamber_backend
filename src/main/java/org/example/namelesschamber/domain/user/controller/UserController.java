package org.example.namelesschamber.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.namelesschamber.common.response.ApiResponse;
import org.example.namelesschamber.domain.auth.core.SecurityUtils;
import org.example.namelesschamber.domain.user.dto.request.NicknameRequestDto;
import org.example.namelesschamber.domain.user.dto.response.UserInfoResponseDto;
import org.example.namelesschamber.domain.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Tag(name = "User", description = "사용자 정보 API")
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

    @Operation(
            summary = "내 정보 조회",
            description = "마이페이지에서 사용할 내 정보를 반환합니다."
    )
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserInfoResponseDto>> getMyInfo() {
        String userId = SecurityUtils.getCurrentSubject();
        UserInfoResponseDto response = userService.getMyInfo(userId);
        return ApiResponse.success(HttpStatus.OK,response);
    }
}
