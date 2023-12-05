package com.acc.jobradar.constants;

public final class RegexConstants {
    private RegexConstants() {
    }

    // Matches with non-alphabets
    public static final String REGEX_NON_ALPHABETS = "[^a-zA-Z ]";
    public static final String SPACE_REGEX = "\\s+";
    public static final String REGEX_ALPHABETS_AND_DASH = "^[a-zA-Z]+(?:[-\\s][a-zA-Z]+)*$";

    public static final String URL_REGEX = "^(http|https)://.*$";
}
