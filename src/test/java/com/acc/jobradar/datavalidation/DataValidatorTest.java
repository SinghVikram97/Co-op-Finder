package com.acc.jobradar.datavalidation;

import com.acc.jobradar.constants.StringConstants;
import com.acc.jobradar.model.JobPosting;
import org.testng.annotations.Test;

import static org.junit.Assert.*;

public class DataValidatorTest
{

    @Test
    public void testValidData()
    {
        JobPosting validData = new JobPosting("Software Engineer", "ABC Inc.", "City", "Description", "http://example.com");
        assertTrue(DataValidator.validateData(validData));
    }

    @Test
    public void testInvalidCompany()
    {
        JobPosting invalidCompany = new JobPosting(StringConstants.JobTitleNotFound, "Invalid Company", "City", "Description", "http://example.com");
        assertFalse(DataValidator.validateData(invalidCompany));
    }

    @Test
    public void testInvalidJobTitle()
    {
        JobPosting invalidJobTitle = new JobPosting("Invalid Job Title", StringConstants.CompanyNotFound, "City", "Description", "http://example.com");
        assertFalse(DataValidator.validateData(invalidJobTitle));
    }

    @Test
    public void testInvalidLocation()
    {
        JobPosting invalidLocation = new JobPosting("Software Engineer", "ABC Inc.", StringConstants.LocationNotFound, "Description", "http://example.com");
        assertFalse(DataValidator.validateData(invalidLocation));
    }

    @Test
    public void testInvalidDescription()
    {
        JobPosting invalidDescription = new JobPosting("Software Engineer", "ABC Inc.", "City", StringConstants.DescriptionNotFound, "http://example.com");
        assertFalse(DataValidator.validateData(invalidDescription));
    }

    @Test
    public void testInvalidWebsiteLink()
    {
        JobPosting invalidWebsiteLink = new JobPosting("Software Engineer", "ABC Inc.", "City", "Description", "Invalid Link");
        assertFalse(DataValidator.validateData(invalidWebsiteLink));
    }

    @Test
    public void testValidWebsiteLink()
    {
        JobPosting validWebsiteLink = new JobPosting("Software Engineer", "ABC Inc.", "City", "Description", "http://example.com");
        assertTrue(DataValidator.validateData(validWebsiteLink));
    }
}
