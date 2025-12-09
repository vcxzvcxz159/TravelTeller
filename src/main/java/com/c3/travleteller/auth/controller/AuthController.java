package com.c3.travleteller.auth.controller;

import com.c3.travleteller.auth.dto.LoginReq;
import com.c3.travleteller.config.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login") // SecurityConfig에서 permitAll() 처리된 경로와 일치해야 함
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping
    public ResponseEntity<String> login(@RequestBody LoginReq loginReq) {

        // 1. UsernamePasswordAuthenticationToken 생성
        // 이 토큰에는 아직 인증되지 않은 사용자 이름과 비밀번호가 담겨있습니다.
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginReq.email(), loginReq.password());

        try {
            // 2. 인증 시도 (CustomUserDetailsService와 PasswordEncoder를 사용하여 검증됨)
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            // 3. 인증 성공 시, JWT 토큰 생성 및 반환
            String jwt = jwtTokenProvider.createToken(authentication);

            return ResponseEntity.ok(jwt);

        } catch (AuthenticationException e) {
            // 인증 실패 (예: 비밀번호 불일치, 사용자 없음)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 정보가 일치하지 않습니다.");
        }
    }
}
