package org.example.namelesschamber.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.namelesschamber.common.response.ApiResponse;
import org.example.namelesschamber.common.security.SecurityUtils;
import org.example.namelesschamber.domain.user.dto.request.LoginRequestDto;
import org.example.namelesschamber.domain.user.dto.request.SignupRequestDto;
import org.example.namelesschamber.domain.user.dto.response.LoginResponseDto;
import org.example.namelesschamber.domain.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Auth", description = "회원가입 및 로그인 API")
public class AuthController {

    private final UserService userService;

    @Operation(
            summary = "회원가입",
            description = "이메일과 비밀번호를 입력받아 회원가입을 수행합니다. 익명 사용자 토큰이 있으면 해당 계정을 회원으로 전환합니다."
    )
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<LoginResponseDto>> signup(
            @Valid @RequestBody SignupRequestDto request) {

        String subject = SecurityUtils.getCurrentSubjectOrEmpty().orElse(null);
        LoginResponseDto response = userService.signup(request, subject);

        return ApiResponse.success(HttpStatus.CREATED, response);
    }

    @Operation(summary = "로그인", description = "이메일과 비밀번호를 입력받아 로그인합니다. 성공 시 회원 정보와 토큰을 반환합니다.")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDto>> login(@Valid @RequestBody LoginRequestDto request) {
        LoginResponseDto response = userService.login(request);
        return ApiResponse.success(HttpStatus.OK, response);
    }

    @Operation(
            summary = "익명 로그인",
            description = "익명 사용자로 로그인합니다. 회원이 아닌 경우에도 JWT 토큰을 발급받아 글쓰기 등 기능을 사용할 수 있습니다."
    )
    @PostMapping("/anonymous")
    public ResponseEntity<ApiResponse<LoginResponseDto>> anonymousLogin() {
        LoginResponseDto response = userService.loginAsAnonymous();
        return ApiResponse.success(HttpStatus.OK, response);
    }

}
