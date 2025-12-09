package com.c3.travleteller.domain.account.repository;

import com.c3.travleteller.domain.account.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

    /**
     * Spring Security의 UserDetailsService에서 사용됩니다.
     * EMAIL 컬럼을 기반으로 AccountEntity를 조회합니다.
     * * SQL: SELECT * FROM tb_account_m WHERE EMAIL = ?
     */
    Optional<AccountEntity> findByEmail(String email);

    /**
     * 소셜 로그인 연동 시 사용됩니다.
     * PROVIDER와 PROVIDER_ID 컬럼을 기반으로 AccountEntity를 조회합니다.
     * * SQL: SELECT * FROM tb_account_m WHERE PROVIDER = ? AND PROVIDER_ID = ?
     */
    Optional<AccountEntity> findByProviderAndProviderId(String provider, String providerId);
}
