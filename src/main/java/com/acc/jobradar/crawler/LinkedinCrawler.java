package com.acc.jobradar.crawler;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class LinkedinCrawler {
    private static final String URL = "https://www.linkedin.com/jobs/search";
    private final ChromeDriver chromeDriver;

    public void getJobPosting(String jobTitle,String location) throws InterruptedException {
        chromeDriver.get(URL);

        Thread.sleep(5000);

        WebElement jobTitleInput = chromeDriver.findElement(By.id("job-search-bar-keywords"));
        WebElement locationInput = chromeDriver.findElement(By.id("job-search-bar-location"));

        jobTitleInput.sendKeys(jobTitle);
        locationInput.clear();
        locationInput.sendKeys(location);

        chromeDriver.findElement(By.xpath("/html/body/div[1]/header/nav/section/section[2]/form/button")).click();

        List<WebElement> jobOpenings = chromeDriver.findElements(By.className("base-search-card--link"));

        List<String> jobLinks=new ArrayList<>();

        jobOpenings.forEach(jobOpening ->{
            String jobLink = jobOpening.findElement(By.tagName("a")).getAttribute("href");
            System.out.println(jobLink);
            jobLinks.add(jobLink);
        });

        // Folder to store HTML files
        String folderPath = "htmlFilesLinkedin";

        // Create the folder if it doesn't exist
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        // Iterate through job links
        for (String jobLink : jobLinks) {
            // Navigate to the job link
            Thread.sleep(5000);
            chromeDriver.get(jobLink);
            Thread.sleep(5000);

            // Get the HTML source
            String htmlSource = chromeDriver.getPageSource();

            // Save HTML to a file
            saveHtmlToFile(htmlSource, folderPath, getFileNameFromUrl(jobLink));
        }
    }

    private static void saveHtmlToFile(String htmlSource, String folderPath, String fileName) {
        try {
            // Create a BufferedWriter to write to the file
            BufferedWriter writer = new BufferedWriter(new FileWriter(folderPath + "/" + fileName + ".html"));

            // Write the HTML source to the file
            writer.write(htmlSource);

            // Close the writer
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getFileNameFromUrl(String url) {
        // Extract the filename from the URL
        return url.substring(url.lastIndexOf('/') + 1);
    }

}
