package com.acc.jobradar.validation;


import com.acc.jobradar.constants.RegexConstants;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchQueryValidator {

    // Checks if searchQuery contains only alphabets or dash
    //  should be between 2 alphabets
    // for example co-op
    private static boolean isJustAlphabetsOrDashBetweenAlphabets(String searchQuery) {
        Pattern pattern = Pattern.compile(RegexConstants.REGEX_ALPHABETS_AND_DASH);
        Matcher matcher = pattern.matcher(searchQuery);
        return matcher.matches();
    }

   // Checks if user query is not an sql query to prevent sql injection
    private static boolean containsSqlKeywords(String searchQuery) {
        // Case-insensitive pattern for identifying common SQL keywords and clauses
        String sqlPattern = "\\b(?:SELECT|INSERT|UPDATE|DELETE|CREATE|ALTER|DROP)\\b.*(FROM|INTO|SET|TABLE|DATABASE)\\b.*";

        // Case-insensitive matching
        Pattern pattern = Pattern.compile(sqlPattern, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(searchQuery);

        return matcher.matches();
    }

    public static boolean validateString(String searchQuery){
        return !searchQuery.isEmpty() && isJustAlphabetsOrDashBetweenAlphabets(searchQuery) && !containsSqlKeywords(searchQuery);
    }
}
