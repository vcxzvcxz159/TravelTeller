package com.c3.travleteller.domain.user.controller;

import com.c3.travleteller.domain.user.dto.RegisteUserReq;
import com.c3.travleteller.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "인증 API", description = "회원가입 및 로그인 처리")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    @Operation(summary = "일반 회원가입", description = "신규 사용자를 등록하고 계정을 생성합니다.") // 💡 Swagger 명세 보강
    public ResponseEntity<String> registerUser(@Valid @RequestBody RegisteUserReq registeUserReq) {
        try {

            userService.registerNewUser(registeUserReq);

            return new ResponseEntity<>("회원가입이 성공적으로 완료되었습니다.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("회원가입 실패: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
