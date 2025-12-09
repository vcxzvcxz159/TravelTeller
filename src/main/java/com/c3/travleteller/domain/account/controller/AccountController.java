package com.c3.travleteller.domain.account.controller;

import com.c3.travleteller.domain.account.entity.AccountEntity;
import com.c3.travleteller.domain.account.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "걔정 API", description = "계정관련 API")
@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/find/{email}")
    @Operation(summary = "일반 회원가입", description = "신규 사용자를 등록하고 계정을 생성합니다.") // 💡 Swagger 명세 보강
    public AccountEntity retrieveAccountByMail(@PathVariable String email) {
        return accountService.retrieveAccountByMail(email);
    }
}
