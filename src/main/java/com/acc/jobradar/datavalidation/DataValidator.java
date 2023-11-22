package com.acc.jobradar.datavalidation;

import com.acc.jobradar.constants.StringConstants;
import com.acc.jobradar.model.JobPosting;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataValidator
{
    public static boolean validateData(JobPosting data)
    {
        boolean containsCompanyName = data.getCompany().equals(StringConstants.CompanyNotFound);
        boolean containsJobTitle = data.getJobTitle().equals(StringConstants.JobTitleNotFound);
        boolean containsLocation = data.getLocation().equals(StringConstants.LocationNotFound);
        boolean containsJobDescription = data.getDescription().equals(StringConstants.DescriptionNotFound);

        boolean isValidWebsiteLink = isValidWebsiteLink(data.getUrl());

        return !containsCompanyName && !containsJobTitle && !containsLocation && !containsJobDescription && isValidWebsiteLink;
    }

    public static boolean isValidWebsiteLink(String input)
    {
        String regex = "^(http|https)://.*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        return matcher.matches();
    }
}
