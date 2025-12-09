package com.c3.travleteller.domain.account.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_account_m")
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AccountEntity {

    @Id
    @Column(name = "ACCOUNT_ID")
    private String accountId;

    @Column(name="USER_ID", length = 32)
    private String userId;

    @Column(name="EMAIL", nullable = false)
    private String email;

    @Column(name="PASSWORD")
    private String password;

    @Column(name="PROVIDER", length = 1,  nullable = false)
    private String provider;

    @Column(name="PROVIDER_ID")
    private String providerId;

    @Column(name="CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

    @Column(name="UPDATED_AT", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
