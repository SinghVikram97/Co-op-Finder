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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class HtmlParserIndeed {
    public List<JobPosting> parseIndeedHtmlFiles() {
        List<JobPosting> jobPostingList=new ArrayList<>();
        String pathFolder = "htmlFilesIndeed";
        Map<String, String> dataExtracted = new HashMap<>();
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
                        // Exception handling
                        exp.printStackTrace();
                    }
                }
            }

        } else {
            System.out.println("The input folder does not exist or is empty!! ");
        }
        return jobPostingList;
    }

    private JobPosting extractJobDetails(Document parsedHtmlDoc) {

        Element jobRoleElement = parsedHtmlDoc.select("h1.jobsearch-JobInfoHeader-title span").first();
        String jobRole = (jobRoleElement != null) ? jobRoleElement.text() : "Job Role not found";

        Element companyNameElement1 = parsedHtmlDoc.select("a.css-1f8zkg3.e19afand0").first();
        Element companyNameElement2 = parsedHtmlDoc.selectFirst("span.css-1cxc9zk a.css-1l2hyrd");
        String companyName = (companyNameElement1 != null) ? companyNameElement1.text() :
                (companyNameElement2 != null) ? companyNameElement2.text() : "Company name not found";

        Element companyLocationElement1 = parsedHtmlDoc.select("div[data-testid=job-location]").first();
        Element companyLocationElement2 = parsedHtmlDoc.selectFirst("div[data-testid='inlineHeader-companyLocation']");
        String companyLocation = (companyLocationElement1 != null) ? companyLocationElement1.text() :
                (companyLocationElement2 != null) ? companyLocationElement2.text() : "Location not found";

        Element websiteLinkElement = parsedHtmlDoc.select("link[rel=canonical]").first();
        String websiteLink = (websiteLinkElement != null) ? websiteLinkElement.attr("href") : "Link not found";

        Element JobDescriptionElement = parsedHtmlDoc.select("#jobDescriptionText").first();
        String jobDescription = (JobDescriptionElement != null) ? JobDescriptionElement.text().trim() : "Description not found";

        return new JobPosting(jobRole, companyName, companyLocation, jobDescription, websiteLink);
    }
}












