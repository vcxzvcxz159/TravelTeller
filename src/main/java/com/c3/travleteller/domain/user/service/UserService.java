package com.c3.travleteller.domain.user.service;

import com.c3.travleteller.domain.account.entity.AccountEntity;
import com.c3.travleteller.domain.account.repository.AccountRepository;
import com.c3.travleteller.domain.user.dto.RegisteUserReq;
import com.c3.travleteller.domain.user.entity.UserEntity;
import com.c3.travleteller.domain.user.repository.UserRepository;
import com.c3.travleteller.util.UuidUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public void registerNewUser(RegisteUserReq registeUserReq) {

        // 1. UUID 생성
        String userId = UuidUtil.generateUuid32();
        String accountId = UuidUtil.generateUuid32();
        String providerId = UuidUtil.generateUuid32();

        // 2. 비밀번호 해싱
        String hashedPassword = passwordEncoder.encode(registeUserReq.password());

        // 3. UserEntity 저장
        UserEntity userEntity = UserEntity.builder()
                .userId(userId)
                .userName(registeUserReq.userName())
                .nickName(registeUserReq.nickname())
                .role("U")
                .birthday(registeUserReq.birthday())
                .gender(registeUserReq.gender())
                .build();

        userRepository.save(userEntity);

        // 4. AcountEntity 저장
        AccountEntity accountEntity = AccountEntity.builder()
                .accountId(accountId)
                .userId(userId)
                .email(registeUserReq.email())
                .password(hashedPassword)
                .provider("L")
                .providerId(providerId)
                .build();

        accountRepository.save(accountEntity);
    }
}
