package com.acc.jobradar.datavalidator;

import com.acc.jobradar.constants.RegexConstants;
import com.acc.jobradar.constants.StringConstants;
import com.acc.jobradar.model.JobPosting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The DataValidator class provides methods for validating JobPosting data.
 */
public class DataValidator {

    /**
     * Validates a JobPosting object by checking essential fields and the validity of the website link.
     *
     * @param data A JobPosting object to be validated.
     * @return true if all fields are valid, otherwise false.
     */
    public static boolean validateJobPosting(JobPosting data) {
        // Check if any essential field contains default or "not found" values
        boolean containsCompanyName = data.getCompany().equals(StringConstants.CompanyNotFound);
        boolean containsJobTitle = data.getJobTitle().equals(StringConstants.JobTitleNotFound);
        boolean containsLocation = data.getLocation().equals(StringConstants.LocationNotFound);
        boolean containsJobDescription = data.getDescription().equals(StringConstants.DescriptionNotFound);

        // Check the validity of the provided website link
        boolean isValidWebsiteLink = isValidWebsiteLink(data.getUrl());

        // Return true if all fields are valid; otherwise, return false
        return !containsCompanyName && !containsJobTitle && !containsLocation && !containsJobDescription && isValidWebsiteLink;
    }

    /**
     * Checks whether the given input string represents a valid website link.
     *
     * @param input The input string representing a website link.
     * @return true if the input is a valid website link, otherwise false.
     */
    private static boolean isValidWebsiteLink(String input) {
        // Define a regex pattern for validating website links starting with "http://" or "https://://"
        String regex = RegexConstants.URL_REGEX;

        // Compile the regex pattern and create a matcher for the input string
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        // Return true if the input matches the regex pattern; otherwise, return false
        return matcher.matches();
    }
}
