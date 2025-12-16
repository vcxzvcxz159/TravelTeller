package com.c3.travleteller.util;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;

import java.security.Key;

public class SecretKeyGenerator {
    public static void main(String[] args) {
        // HS512 알고리즘에 대해 512비트를 충족하는 키를 생성합니다.
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

        // Base64 문자열로 인코딩하여 출력합니다.
        String secureBase64Key = Encoders.BASE64.encode(key.getEncoded());

        System.out.println("=========================================================");
        System.out.println("✅ 새로운 512비트 Base64 Secret Key (길이 86자):");
        System.out.println(secureBase64Key);
        System.out.println("=========================================================");
    }
}
