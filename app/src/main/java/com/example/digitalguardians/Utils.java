package com.example.digitalguardians;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class to extract URLs from text.
 */
public class Utils {
    public static String extractUrl(String text) {
        String regex = "http[s]?://\\S+";
        Matcher matcher = Pattern.compile(regex).matcher(text);
        return matcher.find() ? matcher.group() : "";
    }
}
