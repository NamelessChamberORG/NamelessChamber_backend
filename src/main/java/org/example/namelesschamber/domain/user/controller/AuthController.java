package org.example.namelesschamber.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.namelesschamber.domain.user.dto.request.LoginRequestDto;
import org.example.namelesschamber.domain.user.dto.request.SignupRequestDto;
import org.example.namelesschamber.domain.user.dto.response.LoginResponseDto;
import org.example.namelesschamber.domain.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Auth", description = "회원가입 및 로그인 API")
public class AuthController {

    private final UserService userService;

    @Operation(summary = "회원가입", description = "닉네임과 비밀번호를 입력받아 회원가입을 수행합니다. 성공 시 회원 정보와 토큰을 반환합니다.")
    @PostMapping("/signup")
    public ResponseEntity<LoginResponseDto> signup(@Valid @RequestBody SignupRequestDto request) {
        LoginResponseDto response = userService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "로그인", description = "닉네임과 비밀번호를 입력받아 로그인합니다. 성공 시 회원 정보와 토큰을 반환합니다.")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto request) {
        LoginResponseDto response = userService.login(request);
        return ResponseEntity.ok(response);
    }
}
