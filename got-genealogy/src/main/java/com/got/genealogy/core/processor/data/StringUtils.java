package com.got.genealogy.core.processor.data;

public class StringUtils {

    public static String toSentenceCase(String string) {
        if (string.length() == 0) {
            return string;
        }
        String firstLetter = string.substring(0, 1);
        String restLetters = string.substring(1);
        return firstLetter.toUpperCase() + restLetters.toLowerCase();
    }

    public static String toTitleCase(String string) {
        String[] words = string.split("_|\\s");
        StringBuilder finalString = new StringBuilder();
        for (String word : words) {
            finalString.append(toSentenceCase(word)).append(" ");
        }
        return finalString.toString().trim();
    }

    public static String writeFileExtension(String string, String extension) {
        if (!string.endsWith(extension)) {
            return string + extension;
        }
        return string;
    }
}
