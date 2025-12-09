package com.c3.travleteller.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record RegisteUserReq(
        @NotBlank
        @Email(message = "유효한 이메일 형식이어야 합니다.")
        @Schema(description = "계정 이메일", example = "test@example.com")
        String email,

        @NotBlank
        @Size(min = 8, max = 20)
        @Schema(description = "비밀번호 (8~20자)", example = "password1234!")
        String password,

        @NotBlank
        @Schema(description = "사용자 이름", example = "홍길동")
        String userName,

        @NotBlank
        @Schema(description = "희망 닉네임", example = "여행자")
        String nickname,

        @Schema(description = "생년월일")
        LocalDate birthday,

        @Schema(description = "성별 (남성:M, 여성:F)")
        @Pattern(regexp = "^[MF]?$", message = "성별은 M (남성), F (여성) 또는 빈 값만 입력 가능합니다.")
        String gender
) {}
