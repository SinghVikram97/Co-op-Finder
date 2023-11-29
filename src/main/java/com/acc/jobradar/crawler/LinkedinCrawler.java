package com.acc.jobradar.crawler;

import com.acc.jobradar.filehandler.FileHandler;
import lombok.AllArgsConstructor;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class LinkedinCrawler {
    private static final String URL = "https://www.linkedin.com/jobs/search";
    private final ChromeDriver chromeDriver;
    private final FileHandler fileHandler;

    public void getJobPosting(String jobTitle,String location) throws InterruptedException {
        chromeDriver.get(URL);

        // wait for 5 seconds for the page to load
        chromeDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        // Get input box for job title and location
        WebElement jobTitleInput = chromeDriver.findElement(By.id("job-search-bar-keywords"));
        WebElement locationInput = chromeDriver.findElement(By.id("job-search-bar-location"));

        // Insert job title provided by user into the input box
        jobTitleInput.sendKeys(jobTitle);

        // Clear the default location
        locationInput.clear();
        // Insert location provided by user into input the input box
        locationInput.sendKeys(location);

        // Press the search button
        chromeDriver.findElement(By.xpath("/html/body/div[1]/header/nav/section/section[2]/form/button")).click();

        // Repeat to press more job button 5 times
        for(int i=0;i<5;i++){
            // Maximize the window
            chromeDriver.manage().window().maximize();

            // Scroll down the page to load more jobs
            for (int j = 0; j < 10; j++) {
                JavascriptExecutor js = chromeDriver;
                // First scroll down , then scroll up a little and then scroll down to trigger loading of more jobs
                js.executeScript("window.scrollBy(0,5000)");
                js.executeScript("window.scrollBy(0,-2000)");
                js.executeScript("window.scrollBy(0,5000)");
                // Wait for the jobs to load
                Thread.sleep(5000);
            }

            // Press load more jobs button when we reach the bottom
            chromeDriver.findElement(By.className("infinite-scroller__show-more-button")).click();
        }

        // List to store job links
        List<String> jobLinks=new ArrayList<>();

        // Get the list of divs containing the job details
        List<WebElement> jobOpenings = chromeDriver.findElements(By.className("base-search-card--link"));

        // Extract job link from each div
        jobOpenings.forEach(jobOpening ->{
            try{
                String jobLink = jobOpening.findElement(By.tagName("a")).getAttribute("href");
                jobLinks.add(jobLink);
            }catch (NoSuchElementException | ElementNotInteractableException e){
                System.out.println("No job link found");
            }
        });

        System.out.println("Number of jobs found from Linkedin are: "+jobLinks.size());

        // Folder to store HTML files
        String folderPath = "htmlFilesLinkedin";

        // Create the folder if it doesn't exist
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        // Iterate through job links
        for (String jobLink : jobLinks) {
            // Navigate to job url
            System.out.println("Crawling Linkedin URL: "+jobLink);
            chromeDriver.get(jobLink);

            // Wait for 5 seconds for page to load
            Thread.sleep(5000);

            // Get the HTML source
            String htmlSource = chromeDriver.getPageSource();

            // Save HTML to a file
            fileHandler.saveHtmlToFile(htmlSource, folderPath, fileHandler.getFileNameFromUrl(jobLink));

            // Wait for 5 seconds before navigating to next job url
            Thread.sleep(5000);
        }
    }

}
