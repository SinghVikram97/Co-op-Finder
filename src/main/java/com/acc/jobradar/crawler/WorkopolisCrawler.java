package com.acc.jobradar.crawler;

import lombok.AllArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
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
public class WorkopolisCrawler {
    private static final String URL = "https://www.workopolis.com/en";
    private final ChromeDriver chromeDriver;

    public void getJobPosting(String jobTitle,String location) throws InterruptedException {
        chromeDriver.get(URL);
        WebElement keywordInput = chromeDriver.findElement(By.id("query-input"));
        WebElement locationInput = chromeDriver.findElement(By.id("location-input"));

        keywordInput.sendKeys(jobTitle);
        locationInput.sendKeys(location);

        chromeDriver.findElement(By.xpath("/html/body/div[1]/div/main/section/div[1]/form/button")).click();

        List<String> jobLinks=new ArrayList<>();

//        Pagination-link Pagination-link--next
        int pages=10;
        do{
            chromeDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
            WebElement jobListDiv = chromeDriver.findElement(By.id("job-list"));
            List<WebElement> jobOpenings = jobListDiv.findElements(By.tagName("article"));
            jobOpenings.forEach(jobOpening ->{
                String jobLink = jobOpening.findElement(By.tagName("a")).getAttribute("href");
                jobLinks.add(jobLink);
            });

            chromeDriver.findElement(By.cssSelector(".Pagination-link.Pagination-link--next")).click();
            pages--;
        }while (pages>0);



        // Folder to store HTML files
        String folderPath = "htmlFilesWorkopolis";

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
            System.out.println("Crawling Workopolis URL: "+jobLink);

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
