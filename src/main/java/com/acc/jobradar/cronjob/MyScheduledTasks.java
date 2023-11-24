package com.acc.jobradar.cronjob;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class MyScheduledTasks {

    private final String crawlEndpointUrl = "http://localhost:8080/crawl"; // Update with your actual server URL

    @Scheduled(cron = "0 0 0 * * ?")
    public void crawlTask() {
        System.out.println("Cron Job Started");
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getForObject(crawlEndpointUrl, String.class);
    }
}
