package com.acc.jobradar.parser;

import com.acc.jobradar.constants.StringConstants;
import com.acc.jobradar.datavalidator.DataValidator;
import com.acc.jobradar.model.JobPosting;
import lombok.AllArgsConstructor;

// Jsoup imports
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

// Other required classes imports
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class HtmlParserIndeed {
    public List<JobPosting> parseIndeedHtmlFiles() {
        List<JobPosting> jobPostingList=new ArrayList<>();

        // Providing folder path which contains HTML files
        String pathFolder = "htmlFilesIndeed";

        // HTML files iteration in the folder
        File inputFolderFiles = new File(pathFolder);
        File[] fileList = inputFolderFiles.listFiles();

        if (fileList != null) {
            for (File fileCurrent : fileList) {
                if (fileCurrent.isFile() && fileCurrent.getName().endsWith(".html")) {
                    try {
                        // Parsing HTML file using Jsoup
                        Document parsedDocument = Jsoup.parse(fileCurrent, "UTF-8", "");

                        // Now we need to extract the data from the document parsed (HTML document)
                        JobPosting jobPosting = extractJobDetails(parsedDocument);

                        if(DataValidator.validateJobPosting(jobPosting)) {
                            // Added the extracted data to the jobPostingList
                            jobPostingList.add(jobPosting);
                        }else{
                            System.out.println("Job Posting is invalid, discarding it");
                        }
                    } catch (IOException exp) {
                        // Exception handling
                        exp.printStackTrace();
                    }
                }
            }

        } else {
            // If the input folder does not exist in the specified path, a message will be printed that the file does not exist
            System.out.println("The 'htmlFilesIndeed' folder does not exist or the folder is empty!! ");
        }
        return jobPostingList;
    }

    // This function extracts data from the parsed HTML document
    private JobPosting extractJobDetails(Document parsedHtmlDoc) {

        // To extract "Job Role" data from the HTML file and handling null data
        Element jobRoleElement = parsedHtmlDoc.select("h1.jobsearch-JobInfoHeader-title span").first();
        String jobRole = (jobRoleElement != null) ? jobRoleElement.text() : StringConstants.JobTitleNotFound;

        // To extract "Company Name" data from the HTML file and handling null data
        Element companyNameElement1 = parsedHtmlDoc.select("a.css-1f8zkg3.e19afand0").first();
        Element companyNameElement2 = parsedHtmlDoc.selectFirst("span.css-1cxc9zk a.css-1l2hyrd");
        String companyName = (companyNameElement1 != null) ? companyNameElement1.text() :
                (companyNameElement2 != null) ? companyNameElement2.text() : StringConstants.CompanyNotFound;

        // To extract "Company Location" data from the HTML file and handling null data
        Element companyLocationElement1 = parsedHtmlDoc.select("div[data-testid=job-location]").first();
        Element companyLocationElement2 = parsedHtmlDoc.selectFirst("div[data-testid='inlineHeader-companyLocation']");
        Element companyLocationElement3 = parsedHtmlDoc.selectFirst("div.css-6z8o9s");
        String companyLocation = (companyLocationElement1 != null) ? companyLocationElement1.text() :
                (companyLocationElement2 != null) ? companyLocationElement2.text() : (companyLocationElement3 != null) ? companyLocationElement3.text() : StringConstants.LocationNotFound;

        // To extract "Website URL" data from the HTML file and handling null data
        Element websiteLinkElement = parsedHtmlDoc.select("link[rel=canonical]").first();
        String websiteLink = (websiteLinkElement != null) ? websiteLinkElement.attr("href") : StringConstants.LinkNotFound;

        // To extract "Job Description" data from the HTML file and handling null data
        Element JobDescriptionElement = parsedHtmlDoc.select("#jobDescriptionText").first();
        String jobDescription = (JobDescriptionElement != null) ? JobDescriptionElement.text().trim() : StringConstants.DescriptionNotFound;

        // Return all the extracted elements to the 'IndeedJobData' class
        return new JobPosting(jobRole, companyName, companyLocation, jobDescription, websiteLink);
    }
}












