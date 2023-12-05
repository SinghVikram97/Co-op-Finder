package com.acc.jobradar.crawler;

import com.acc.jobradar.filehandler.FileHandler;
import lombok.AllArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
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
public class IndeedCrawler {
    private static final String URL = "https://ca.indeed.com/jobs?";
    private final ChromeDriver chromeDriver;
    private final Logger logger = LoggerFactory.getLogger(IndeedCrawler.class);


    public void getJobPosting(String jobTitle, String location) throws InterruptedException {
        // Open Indeed with job title and location provided by the user embedded in the URL
        chromeDriver.get(URL + "q=" + jobTitle + "&l=" + location);

        // List to store job link
        List<String> jobLinks = new ArrayList<>();

        // Crawl 20 pages
        int pages = 20;

        try {
            do {
                // wait for 5 seconds before loading the next page
                chromeDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

                // In case it asks the user to log in close the popup
                try {
                    chromeDriver.findElement(By.cssSelector(".css-yi9ndv.e8ju0x51")).click();
                } catch (NoSuchElementException e) {
                    logger.info("No popup present");
                }

                // Get each individual job posting card
                List<WebElement> jobOpenings = chromeDriver.findElements(By.className("resultContent"));

                // For each job posting extract its link
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

                // Find the next button and click on it to navigate to the next page
                List<WebElement> elements = chromeDriver.findElements(By.cssSelector(".css-akkh0a.e8ju0x50"));
                if (elements.size() == 1) {
                    elements.get(0).click();
                } else {
                    elements.get(1).click();
                }

                pages--;
            } while (pages > 0);
        } catch (WebDriverException webDriverException) {
            logger.error("Exception while crawling Indeed, cause: {}, message: {}",
                    webDriverException.getCause(),
                    webDriverException.getMessage());
        }


        // Folder to store HTML files
        String folderPath = "htmlFilesIndeed";

        // Create the folder if it doesn't exist
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        // Iterate through job links
        for (String jobLink : jobLinks) {
            // Navigate to job url
            chromeDriver.get(jobLink);

            // wait for html to be loaded
            Thread.sleep(5000);

            logger.info("Visiting Indeed URL: " + jobLink);

            // Get the HTML source
            String htmlSource = chromeDriver.getPageSource();

            // Save HTML to a file
            FileHandler.saveHtmlToFile(htmlSource, folderPath, FileHandler.getFileNameFromUrl(jobLink));
        }
    }
}
