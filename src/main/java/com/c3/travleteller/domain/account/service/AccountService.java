package com.c3.travleteller.domain.account.service;

import com.c3.travleteller.domain.account.entity.AccountEntity;
import com.c3.travleteller.domain.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountEntity retrieveAccountByMail(String email) {
        return accountRepository.findByEmail(email).orElse(null);
    }
}
