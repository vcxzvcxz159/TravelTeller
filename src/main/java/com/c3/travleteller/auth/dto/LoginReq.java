package com.c3.travleteller.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginReq(
        @NotBlank
        @Email(message = "유효한 이메일 형식이어야 합니다.")
        @Schema(description = "계정 이메일", example = "test@example.com")
        String email,

        @NotBlank
        @Size(min = 8, max = 20)
        @Schema(description = "비밀번호 (8~20자)", example = "password1234!")
        String password
) {
}
