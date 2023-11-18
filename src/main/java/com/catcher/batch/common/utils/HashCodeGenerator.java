package com.catcher.batch.common.utils;

import org.apache.commons.codec.digest.DigestUtils;

public class HashCodeGenerator {
    public static String hashString(String category, String input) {
        return DigestUtils.sha256Hex(category + "-" + input);
    }

    public static String hashString(String input) {
        return DigestUtils.sha256Hex(input);
    }
}
