package com.acc.jobradar.crawler;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class IndeedCrawler {
    private static final String URL = "https://ca.indeed.com/";
    private final ChromeDriver chromeDriver;

    public void getJobPosting(String jobTitle,String location) throws InterruptedException {
        chromeDriver.get(URL);

        Thread.sleep(2000);

        WebElement jobTitleInput = chromeDriver.findElement(By.id("text-input-what"));
        WebElement locationInput = chromeDriver.findElement(By.id("text-input-where"));

        jobTitleInput.sendKeys(jobTitle);

        locationInput.sendKeys(Keys.CONTROL, Keys.chord("a"));
        for (int i = 0; i < 11; i++) {
            locationInput.sendKeys(Keys.BACK_SPACE);
        }
        locationInput.sendKeys(location);

        WebElement formElement = chromeDriver.findElement(By.id("jobsearch"));
        WebElement submitButton = formElement.findElement(By.tagName("button"));

        Thread.sleep(3000);

        submitButton.click();

        List<WebElement> jobOpenings = chromeDriver.findElements(By.className("resultContent"));

        List<String> jobLinks=new ArrayList<>();

        jobOpenings.forEach(jobOpening ->{
            String jobLink = jobOpening.findElement(By.tagName("a")).getAttribute("href");
            jobLinks.add(jobLink);
        });

        // Folder to store HTML files
        String folderPath = "htmlFilesIndeed";

        // Create the folder if it doesn't exist
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        // Iterate through job links
        for (String jobLink : jobLinks) {
            chromeDriver.get(jobLink);
            // wait for html to be loaded
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
        return Integer.toHexString(url.hashCode());
    }
}
