package com.got.genealogy.core.processor;

public class StringUtils {

    public static String toSentenceCase(String string) {
        String firstLetter = string.substring(0, 1);
        String restLetters = string.substring(1);
        return firstLetter.toUpperCase() + restLetters.toLowerCase();
    }
}
