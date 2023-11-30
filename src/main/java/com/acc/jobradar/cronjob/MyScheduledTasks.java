package com.acc.jobradar.cronjob;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class MyScheduledTasks {

    private final String crawlEndpointUrl = "http://localhost:8080/crawl"; // Update with your actual server URL

    /**
     * @Scheduled(cron = "second minute hour day month dayOfWeek year")
     *
     * 0 for seconds (the job will run at the beginning of the minute).
     * 0 for minutes (the job will run at the beginning of the hour).
     * 0 for hours (the job will run at midnight).
     * * for the day of the month (no specific day is specified).
     * * for the month (the job will run every month).
     * ? for the day of the week (no specific day is specified).
     * * for the year (the job will run every year).
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void crawlTask() {
        System.out.println("Cron Job Started");
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getForObject(crawlEndpointUrl, String.class);
    }
}
