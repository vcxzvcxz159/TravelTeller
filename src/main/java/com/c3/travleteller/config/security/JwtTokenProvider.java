package com.c3.travleteller.config.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider implements InitializingBean {

    private static final String AUTHORITIES_KEY = "auth";

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.token-validity-in-seconds}")
    private long tokenValidityInMilliseconds;

    private Key key;

    @Override
    public void afterPropertiesSet() {
        // Base64 디코딩
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        // Keys.hmacShaKeyFor를 사용하여 Key 객체 생성 (HS512 알고리즘 사용)
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // 1. JWT 토큰 생성
    public String createToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validity = new Date(now + this.tokenValidityInMilliseconds); // 만료 시간 설정

        return Jwts.builder()
                .setSubject(authentication.getName()) // 토큰 주체 (사용자 ID 또는 이메일)
                .claim(AUTHORITIES_KEY, authorities) // "auth" claim에 권한 정보 추가
                .signWith(key, SignatureAlgorithm.HS512) // HS512 알고리즘으로 서명
                .setExpiration(validity) // 만료 시간
                .compact();
    }

    /**
     * 2. 토큰에서 인증 정보 조회 (JwtAuthenticationFilter에서 사용)
     * @param token 유효한 JWT 토큰
     * @return Authentication 객체
     */
    public Authentication getAuthentication(String token) {
        // 1. 토큰에서 Claims 추출
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key) // 서명 키를 사용하여 토큰 파싱
                .build()
                .parseClaimsJws(token)
                .getBody();

        // ------------------ 🪵 토큰 내용 로그 출력 시작 ------------------
        String username = claims.getSubject();
        Object authoritiesClaim = claims.get(AUTHORITIES_KEY);

        log.info("✅ JWT 토큰 정보 추출 성공:");
        log.info("  - Subject (Username/Email): {}", username);
        log.info("  - Authorities (권한): {}", authoritiesClaim);
        // ------------------ 🪵 토큰 내용 로그 출력 끝 ------------------

        // 2. Claims에서 권한 정보 추출
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // 3. UserDetails 객체 생성 (비밀번호는 필요 없으므로 빈 문자열로 설정)
        UserDetails principal = new User(claims.getSubject(), "", authorities);

        // 4. Authentication 객체 반환
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    /**
     * 3. 토큰 유효성 검증
     * @param token 검증할 JWT 토큰
     * @return 유효성 여부 (boolean)
     */
    public boolean validateToken(String token) {
        try {
            // 서명 키로 토큰을 파싱 시도. 성공하면 유효함.
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.", e);
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.", e);
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.", e);
        }
        return false;
    }
}
