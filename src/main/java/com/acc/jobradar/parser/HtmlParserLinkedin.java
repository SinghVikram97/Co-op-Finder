package com.acc.jobradar.parser;

import com.acc.jobradar.model.JobPosting;
import lombok.AllArgsConstructor;

// Jsoup imports
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

// Other required classes imports
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class HtmlParserLinkedin {
    public List<JobPosting> parseLinkedinHtmlFiles() {
        List<JobPosting> jobPostingList = new ArrayList<>();

        // Providing folder path which contains HTML files
        String pathFolder = "htmlFilesLinkedin";

        // HTML files iteration in the folder
        File inputFolderFiles = new File(pathFolder);
        File[] fileList = inputFolderFiles.listFiles();

        if (fileList != null) {
            for (File fileCurrent : fileList) {
                // Checking if the file is a valid HTML file
                if (fileCurrent.isFile() && fileCurrent.getName().endsWith(".html")) {
                    try {
                        // Parsing HTML file using Jsoup
                        Document parsedDocument = Jsoup.parse(fileCurrent, "UTF-8", "");

                        // Now we need to extract the data from the document parsed (HTML document)
                        JobPosting jobPosting = extractJobDetails(parsedDocument);

                        // The extracted data is added to the jobPostingList
                        jobPostingList.add(jobPosting);
                    } catch (IOException exp) {
                        // Exception handling
                        exp.printStackTrace();
                    }
                }
            }
        } else {
            // If the input folder does not exist in the specified path, a message will be printed that the file does not exist
            System.out.println("The 'htmlFilesLinkedin' folder does not exist or the folder is empty!! ");
        }
        return jobPostingList;
    }

    // To extract data from the parsed HTML document
    private static JobPosting extractJobDetails(Document parsedHtmlDoc) {

        // To extract "Job Role" data from the HTML file and handling null data
        Element jobRoleElement = parsedHtmlDoc.select(".top-card-layout__title").first();
        String jobRole = (jobRoleElement != null) ? jobRoleElement.text() : "Job title not found";

        // To extract "Company name" data from the HTML file and handling null data
        Element companyNameElement = parsedHtmlDoc.select(".topcard__org-name-link").first();
        String companyName = (companyNameElement != null) ? companyNameElement.text().replace("Company Name: ", "") : "Company name not found";

        // To extract "Company Location" data from the HTML file and handling null data
        Element companyLocationElement = parsedHtmlDoc.select(".topcard__flavor--bullet").first();
        String companyLocation = (companyLocationElement != null) ? companyLocationElement.text().trim() : "Location not found";

        // To extract "Job Description" data from the HTML file and handling null data
        Element JobDescriptionElement = parsedHtmlDoc.select(".description__text--rich .show-more-less-html__markup").first();
        String jobDescription = (JobDescriptionElement != null) ? JobDescriptionElement.text().trim() : "Description not found";

        // To extract "Website URL" data from the HTML file and handling null data
        Element websiteLinkElement = parsedHtmlDoc.select("link[rel=canonical]").first();
        String websiteLink = (websiteLinkElement != null) ? websiteLinkElement.attr("href") : "Link not found";

        // Return all the extracted elements to the 'JobPosting' class
        return new JobPosting(jobRole, companyName, companyLocation, jobDescription, websiteLink);
    }
}


