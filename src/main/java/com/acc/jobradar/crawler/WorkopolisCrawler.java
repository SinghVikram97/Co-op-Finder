package com.acc.jobradar.crawler;

import com.acc.jobradar.filehandler.FileHandler;
import lombok.AllArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class WorkopolisCrawler {
    private static final String URL = "https://www.workopolis.com/en";
    private final ChromeDriver chromeDriver;

    private final Logger logger = LoggerFactory.getLogger(WorkopolisCrawler.class);

    public void getJobPosting(String jobTitle, String location) throws InterruptedException {
        // Go to Workopolis
        chromeDriver.get(URL);

        // List to store job links
        List<String> jobLinks = new ArrayList<>();

        try {
            // Get the input box for keyword and location
            WebElement keywordInput = chromeDriver.findElement(By.id("query-input"));
            WebElement locationInput = chromeDriver.findElement(By.id("location-input"));

            //  Insert job title and location provided by user into the input box
            keywordInput.sendKeys(jobTitle);
            locationInput.sendKeys(location);

            // Press the find jobs button
            chromeDriver.findElement(By.xpath("/html/body/div[1]/div/main/section/div[1]/form/button")).click();

            // Crawl 10 pages of search results
            int pages = 10;
            do {
                // Find the div which contains the list of jobs
                WebElement jobListDiv = chromeDriver.findElement(By.id("job-list"));
                // Find each job opening's individual card
                List<WebElement> jobOpenings = jobListDiv.findElements(By.tagName("article"));
                // For each job opening get the link to the job opening and add it in the list
                jobOpenings.forEach(jobOpening -> {
                    try {
                        String jobLink = jobOpening.findElement(By.tagName("a")).getAttribute("href");
                        jobLinks.add(jobLink);
                    } catch (WebDriverException webDriverException) {
                        logger.error("Error while getting job link, cause: {}, message: {}, continuing onto the next job posting",
                                webDriverException.getCause(),
                                webDriverException.getMessage());
                    }

                });

                // Go to the next page by pressing the next page button
                chromeDriver.findElement(By.cssSelector(".Pagination-link.Pagination-link--next")).click();

                pages--;

                // wait for 5 seconds before loading the next page
                chromeDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
            } while (pages > 0);
        } catch (WebDriverException webDriverException) {
            logger.error("Exception while crawling Workopolis, cause: {}, message: {}",
                    webDriverException.getCause(),
                    webDriverException.getMessage());
        }

        // Folder to store HTML files
        String folderPath = "htmlFilesWorkopolis";

        // Create the folder if it doesn't exist
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        // Iterate through job links
        for (String jobLink : jobLinks) {
            // Navigate to job url
            chromeDriver.get(jobLink);

            // wait for 5 seconds for the page to be loaded
            Thread.sleep(5000);

            logger.info("Visiting Workopolis URL: " + jobLink);

            // Get the HTML source
            String htmlSource = chromeDriver.getPageSource();

            // Save HTML to a file
            FileHandler.saveHtmlToFile(htmlSource, folderPath, FileHandler.getFileNameFromUrl(jobLink));
        }
    }

}
