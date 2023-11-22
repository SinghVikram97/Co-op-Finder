package com.acc.jobradar.parser;

import com.acc.jobradar.constants.StringConstants;
import com.acc.jobradar.datavalidation.DataValidator;
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
public class HtmlParserLinkedin {
    public List<JobPosting> parseLinkedinHtmlFiles() {
        List<JobPosting> jobPostingList = new ArrayList<>();
        String pathFolder = "htmlFilesLinkedin";
        File inputFolderFiles = new File(pathFolder);
        File[] fileList = inputFolderFiles.listFiles();

        if (fileList != null) {
            for (File fileCurrent : fileList) {
                if (fileCurrent.isFile() && fileCurrent.getName().endsWith(".html")) {
                    try {
                        Document parsedDocument = Jsoup.parse(fileCurrent, "UTF-8", "");
                        JobPosting jobPosting = extractJobDetails(parsedDocument);
                        if(DataValidator.validateData(jobPosting)){
                            jobPostingList.add(jobPosting);
                        }
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

        Element jobRoleElement = parsedHtmlDoc.select(".top-card-layout__title").first();
        String jobRole = (jobRoleElement != null) ? jobRoleElement.text() : StringConstants.JobTitleNotFound;

        Element companyNameElement = parsedHtmlDoc.select(".topcard__org-name-link").first();
        String companyName = (companyNameElement != null) ? companyNameElement.text().replace("Company Name: ", "") : StringConstants.CompanyNotFound;

        Element companyLocationElement = parsedHtmlDoc.select(".topcard__flavor--bullet").first();
        String companyLocation = (companyLocationElement != null) ? companyLocationElement.text().trim() : StringConstants.LocationNotFound;

        Element JobDescriptionElement = parsedHtmlDoc.select(".description__text--rich .show-more-less-html__markup").first();
        String jobDescription = (JobDescriptionElement != null) ? JobDescriptionElement.text().trim() : StringConstants.DescriptionNotFound;

        Element websiteLinkElement = parsedHtmlDoc.select("link[rel=canonical]").first();
        String websiteLink = (websiteLinkElement != null) ? websiteLinkElement.attr("href") : StringConstants.LinkNotFound;

        return new JobPosting(jobRole, companyName, companyLocation, jobDescription, websiteLink);
    }
}


