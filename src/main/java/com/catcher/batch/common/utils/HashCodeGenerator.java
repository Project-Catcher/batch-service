package com.catcher.batch.common.utils;

import org.apache.commons.codec.digest.DigestUtils;

public class HashCodeGenerator {
    public static String hashString(String input) {
        return DigestUtils.sha256Hex(input);
    }

    public static String hashString(Object... objects) {
        StringBuilder sb = new StringBuilder();
        for (Object object : objects) {
            sb.append(object.toString());
        }
        return DigestUtils.sha256Hex(sb.toString());
    }
}
