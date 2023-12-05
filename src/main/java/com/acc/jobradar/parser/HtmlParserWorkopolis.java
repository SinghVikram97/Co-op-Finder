package com.acc.jobradar.parser;

import com.acc.jobradar.constants.StringConstants;
import com.acc.jobradar.datavalidator.DataValidator;
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
public class HtmlParserWorkopolis {
    public List<JobPosting> parseWorkopolisHtmlFiles() {
        List<JobPosting> jobPostingList = new ArrayList<>();

        // Providing folder path which contains HTML files
        String pathFolder = "htmlFilesWorkopolis";

        // HTML files iteration in the folder
        File inputFolderFiles = new File(pathFolder);
        File[] fileList = inputFolderFiles.listFiles();

        if (fileList != null) {
            for (File fileCurrent : fileList) {
                // Checking if the file is a valid HTML file
                if (fileCurrent.isFile() && fileCurrent.getName().endsWith(".html")) {
                    try {
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
                        // Handling Exception
                        exp.printStackTrace();
                    }
                }
            }

        } else {
            // If the input folder does not exist in the specified path, a message will be printed that the file does not exist
            System.out.println("The 'htmlFilesWorkopolis' folder does not exist or the folder is empty!! ");
        }
        return jobPostingList;
    }

    // To extract data from the parsed HTML document
    private static JobPosting extractJobDetails(Document parsedHtmlDoc) {
        // To extract "Job Role" data from the HTML file and handling null data
        Element jobRoleElement = parsedHtmlDoc.select("div.ViewJobHeaderTitle").first();
        String jobRole = (jobRoleElement != null) ? jobRoleElement.text() : StringConstants.JobTitleNotFound;

        // To extract "Company name" data from the HTML file and handling null data
        Element companyNameElement = parsedHtmlDoc.select("div.ViewJobHeaderCompany").first();
        String companyName = (companyNameElement != null) ? companyNameElement.text().replace("Company Name: ", "") : StringConstants.CompanyNotFound;

        // To extract "Company Location" data from the HTML file and handling null data
        Element companyLocationElement = parsedHtmlDoc.select(".ViewJobHeaderPropertiesLocation").first();
        String companyLocation = (companyLocationElement != null) ? companyLocationElement.text().trim() : StringConstants.LocationNotFound;

        // To extract "Job Description" data from the HTML file and handling null data
        Element JobDescriptionElement = parsedHtmlDoc.select(".viewjob-description-tab .ViewJobBodyDescription").first();
        String jobDescription = (JobDescriptionElement != null) ? JobDescriptionElement.text().trim() : StringConstants.DescriptionNotFound;

        // To extract "Website URL" data from the HTML file and handling null data
        Element websiteLinkElement = parsedHtmlDoc.select("link[rel=canonical]").first();
        String websiteLink = (websiteLinkElement != null) ? websiteLinkElement.attr("href") : StringConstants.LinkNotFound;

        // Return all the extracted elements to the 'WorkopolisJobData' class
        return new JobPosting(jobRole, companyName, companyLocation, jobDescription, websiteLink);
    }
}
