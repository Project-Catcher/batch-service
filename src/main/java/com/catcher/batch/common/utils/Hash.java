package com.catcher.batch.common.utils;

import java.util.Objects;

public class Hash {
    public static boolean isUpdated(int itemHash, int responseHash) {
        return itemHash != responseHash;
    }

    public static int hashGenerator(Object... values) {
        return Objects.hash(values);
    }
}
