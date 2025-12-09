package com.c3.travleteller.util;

import java.util.UUID;

public class UuidUtil {

    public static String generateUuid() {
        return UUID.randomUUID().toString();
    }

    public static String  generateUuid32() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
