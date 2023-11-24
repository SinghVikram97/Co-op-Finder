package com.acc.jobradar.crawler;

import lombok.AllArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class LinkedinCrawler {
    private static final String URL = "https://www.linkedin.com/jobs/search";
    private final ChromeDriver chromeDriver;

    public void getJobPosting(String jobTitle,String location) throws InterruptedException {
        chromeDriver.get(URL);

        chromeDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        WebElement jobTitleInput = chromeDriver.findElement(By.id("job-search-bar-keywords"));
        WebElement locationInput = chromeDriver.findElement(By.id("job-search-bar-location"));

        jobTitleInput.sendKeys(jobTitle);
        locationInput.clear();
        locationInput.sendKeys(location);

        chromeDriver.findElement(By.xpath("/html/body/div[1]/header/nav/section/section[2]/form/button")).click();

        int pages = 10;
        List<String> jobLinks=new ArrayList<>();

        do {
            chromeDriver.manage().window().maximize();

            for (int i = 0; i < 10; i++) {
                JavascriptExecutor js = chromeDriver;
                js.executeScript("window.scrollBy(0,5000)");
                js.executeScript("window.scrollBy(0,-2000)");
                js.executeScript("window.scrollBy(0,5000)");
                Thread.sleep(5000);
            }


            List<WebElement> jobOpenings = chromeDriver.findElements(By.className("base-search-card--link"));

            jobOpenings.forEach(jobOpening ->{
                String jobLink = jobOpening.findElement(By.tagName("a")).getAttribute("href");
                jobLinks.add(jobLink);
            });

            pages--;

            chromeDriver.findElement(By.className("infinite-scroller__show-more-button")).click();

        }while(pages>0);


        if(jobLinks.isEmpty()) {
            System.out.println("NO job links found");
        }

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
            System.out.println("Crawling Linkedin URL: "+jobLink);
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
        return Integer.toHexString(url.hashCode());
    }

}
