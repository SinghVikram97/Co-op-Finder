package com.acc.jobradar.validation;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchQueryValidator {

    /**
     * The Java function `isJustSymbolOrNumbers` validates a non-null, non-empty string, checking that it
     * comprises only symbols or numbers. It returns `true` if the criteria are met; otherwise, it returns `false`.
     * This function is useful for ensuring input adherence to a specific character set in programming contexts.
     * @param searchQuery
     * @return
     */
    private static boolean isJustSymbolOrNumbers(String searchQuery) {
        if (searchQuery == null || searchQuery.trim().isEmpty()) {
            return false;
        }

        Pattern pattern = Pattern.compile("^[^a-zA-Z\\s]+$");
        Matcher matcher = pattern.matcher(searchQuery);

        return matcher.matches();
    }

    /**
     * The `isJustCharactersWithNumbers` Java function validates a non-null, non-empty string, ensuring it
     * contains only alphanumeric characters and spaces. The function returns `true` if the criteria are met;
     * otherwise, it returns `false`. This basic data validation aids in verifying user inputs conform to a
     * specific character set.
     * @param searchQuery
     * @return
     */
    private static boolean isJustCharactersWithNumbers(String searchQuery) {
        if (searchQuery == null || searchQuery.trim().isEmpty()) {
            return false;
        }

        Pattern pattern = Pattern.compile("^[a-zA-Z0-9\\s]+$");
        Matcher matcher = pattern.matcher(searchQuery);

        return matcher.matches();
    }

    /**
     * The regular expression checks for SQL keywords (SELECT, INSERT, UPDATE, DELETE, CREATE, ALTER, DROP)
     * followed by associated clauses (FROM, INTO, SET, TABLE, DATABASE) with word boundary assertions.
     * @param input
     * @return
     */
    private static boolean containsSqlKeywords(String input) {
        // Case-insensitive pattern for identifying common SQL keywords and clauses
        String sqlPattern = "\\b(?:SELECT|INSERT|UPDATE|DELETE|CREATE|ALTER|DROP)\\b.*(FROM|INTO|SET|TABLE|DATABASE)\\b.*";

        // Case-insensitive matching
        Pattern pattern = Pattern.compile(sqlPattern, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(input);

        return matcher.matches();
    }

    public static boolean validateString(String input){
        if(!input.trim().isEmpty() && !isJustSymbolOrNumbers(input) && isJustCharactersWithNumbers(input)){
            if(containsSqlKeywords(input)){
                return false;
            }
            return true;
        }
        return false;
    }
}
