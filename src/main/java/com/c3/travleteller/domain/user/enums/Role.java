package com.c3.travleteller.domain.user.enums;

import java.util.Arrays;

public enum Role {

    ADMIN("A", "ROLE_ADMIN"),
    USER("U", "ROLE_USER");

    private String roleCode;
    private String authority;

    Role(String roleCode, String authority) {
        this.roleCode = roleCode;
        this.authority = authority;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public String getAuthority() {
        return authority;
    }

    public static Role fromRoleCode(String roleCode) {
        return Arrays.stream(Role.values())
                .filter(role -> role.roleCode.equals(roleCode))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("지원되지 않는 역할 코드입니다: " + roleCode));
    }
}
