package com.got.genealogy.core.processor.data;

/**
 * StringUtils for converting Strings and
 * applying other quick processes onto them.
 */
public class StringUtils {

    /**
     * Returns a String in Sentence case.
     *
     * @param string input String.
     * @return a String in Sentence case.
     */
    public static String toSentenceCase(String string) {
        if (string.length() == 0) {
            return string;
        }
        String firstLetter = string.substring(0, 1);
        String restLetters = string.substring(1);
        return firstLetter.toUpperCase() + restLetters.toLowerCase();
    }

    /**
     * Returns a String in Title Case.
     *
     * @param string input String.
     * @return a String in Title Case.
     */
    public static String toTitleCase(String string) {
        String[] words = string.split("_|\\s");
        StringBuilder finalString = new StringBuilder();
        for (String word : words) {
            finalString.append(toSentenceCase(word)).append(" ");
        }
        return finalString.toString().trim();
    }

    /**
     * Returns a String with a file extension,
     * after checking if it already ends with
     * one.
     *
     * @param string    input filename String.
     * @param extension file extension to be
     *                  appended onto the
     *                  filename.
     * @return a String with a file extension,
     * after checking if it already ends with
     * one.
     */
    public static String writeFileExtension(String string, String extension) {
        if (!string.endsWith(extension)) {
            return string + extension;
        }
        return string;
    }
}
