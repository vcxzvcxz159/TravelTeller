package com.c3.travleteller.domain.user.service;

import com.c3.travleteller.domain.account.entity.AccountEntity;
import com.c3.travleteller.domain.account.repository.AccountRepository;
import com.c3.travleteller.domain.user.entity.UserEntity;
import com.c3.travleteller.domain.user.enums.Role;
import com.c3.travleteller.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final UserRepository  userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 1. DB에서 해당 EMAIL을 가진 Account 정보를 조회
        AccountEntity account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("해당 이메일로 등록된 사용자가 없습니다: " + email));

        // 2. AccountEntity 내의 user 객체를 통해 role값 확인 ("U" : User, "A" : Admin)
        UserEntity user = userRepository.findByUserId(account.getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("해당 유저ID로 등록된 사용자가 없습니다: " + account.getUserId()));
        String role = user.getRole();
        Role userRole = Role.fromRoleCode(role);

        // 3. ROLE 문자열을 Spring Security의 권한 객체로 변환합니다.
        // 예: "ADMIN" -> "ROLE_ADMIN"
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(userRole.getAuthority()));

        // 2. 조회된 정보를 Spring Security가 요구하는 UserDetails 객체로 변환합니다.
        return new User(
                account.getEmail(), // 아이디
                account.getPassword(), // 비밀번호
                authorities
        );
    }
}
