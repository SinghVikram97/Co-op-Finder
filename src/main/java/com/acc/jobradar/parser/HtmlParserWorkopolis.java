package com.acc.jobradar.parser;

import com.acc.jobradar.model.JobPosting;
import lombok.AllArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class HtmlParserWorkopolis {
    public List<JobPosting> parseWorkopolisHtmlFiles() {
        List<JobPosting> jobPostingList = new ArrayList<>();
        String pathFolder = "htmlFilesWorkopolis";

        File inputFolderFiles = new File(pathFolder);
        File[] fileList = inputFolderFiles.listFiles();

        if (fileList != null) {
            for (File fileCurrent : fileList) {
                if (fileCurrent.isFile() && fileCurrent.getName().endsWith(".html")) {
                    try {
                        Document parsedDocument = Jsoup.parse(fileCurrent, "UTF-8", "");
                        JobPosting jobPosting = extractJobDetails(parsedDocument);
                        jobPostingList.add(jobPosting);
                    } catch (IOException exp) {
                        exp.printStackTrace();
                    }
                }
            }

        } else {
            System.out.println("The input folder does not exist or is empty!! ");
        }
        return jobPostingList;
    }

    private static JobPosting extractJobDetails(Document parsedHtmlDoc) {

        Element jobRoleElement = parsedHtmlDoc.select("div.ViewJobHeaderTitle").first();
        String jobRole = (jobRoleElement != null) ? jobRoleElement.text() : "Job title not found";

        Element companyNameElement = parsedHtmlDoc.select("div.ViewJobHeaderCompany").first();
        String companyName = (companyNameElement != null) ? companyNameElement.text().replace("Company Name: ", "") : "Company name not found";

        Element companyLocationElement = parsedHtmlDoc.select(".ViewJobHeaderPropertiesLocation").first();
        String companyLocation = (companyLocationElement != null) ? companyLocationElement.text().trim() : "Location not found";

        Element JobDescriptionElement = parsedHtmlDoc.select(".viewjob-description-tab .ViewJobBodyDescription").first();
        String jobDescription = (JobDescriptionElement != null) ? JobDescriptionElement.text().trim() : "Description not found";

        Element websiteLinkElement = parsedHtmlDoc.select("link[rel=canonical]").first();
        String websiteLink = (websiteLinkElement != null) ? websiteLinkElement.attr("href") : "Link not found";

        // Return all the extracted elements to the 'WorkopolisJobData' class
        return new JobPosting(jobRole, companyName, companyLocation, jobDescription, websiteLink);
    }
}


