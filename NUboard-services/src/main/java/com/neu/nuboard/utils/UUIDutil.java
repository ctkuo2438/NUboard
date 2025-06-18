package com.neu.nuboard.utils;

import java.util.UUID;

public class UUIDutil {
    public static String getId() {
        return java.util.UUID.randomUUID().toString(); // psql varchar(36)
    }
}
