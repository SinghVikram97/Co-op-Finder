package com.acc.jobradar.crawler;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
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
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class IndeedCrawler {
    private static final String URL = "https://ca.indeed.com/jobs?";
    private final ChromeDriver chromeDriver;

    public void getJobPosting(String jobTitle,String location) throws InterruptedException {
        chromeDriver.get(URL+"q="+jobTitle+"&l="+location);
        List<String> jobLinks=new ArrayList<>();

        int pages=10;

        do{
            chromeDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
            try{
                chromeDriver.findElement(By.cssSelector(".css-yi9ndv.e8ju0x51")).click();
            }catch (NoSuchElementException e){
                System.out.println("No element");
            }
            List<WebElement> jobOpenings = chromeDriver.findElements(By.className("resultContent"));
            jobOpenings.forEach(jobOpening ->{
                String jobLink = jobOpening.findElement(By.tagName("a")).getAttribute("href");
                jobLinks.add(jobLink);
            });
            System.out.println(jobLinks.size());
            List<WebElement> elements = chromeDriver.findElements(By.cssSelector(".css-akkh0a.e8ju0x50"));
            if(elements.size()==1){
                elements.get(0).click();
            }
            else{
                elements.get(1).click();
            }
            pages--;
        }while (pages>0);



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
            System.out.println("Crawling Indeed URL: "+jobLink);


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
