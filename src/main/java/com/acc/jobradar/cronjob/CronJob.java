package com.acc.jobradar.cronjob;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CronJob {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${service.url:http://localhost:8080}")  // Default value is http://localhost:8080
    private String serviceUrl;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        // Call /buildSearchEngine endpoint on service startup
        String buildSearchEngineEndpoint = serviceUrl + "/buildsearchengine";
        restTemplate.getForObject(buildSearchEngineEndpoint, String.class);
    }

    @Scheduled(cron = "0 0 1 * * SUN") // Every Sunday at 1 am
    public void runLinkedinCrawler() {
        // Call /crawlLinkedin endpoint
        String crawlLinkedinEndpoint = serviceUrl + "/crawlLinkedin";
        restTemplate.getForObject(crawlLinkedinEndpoint, String.class);

        // Call /buildsearchengine endpoint
        String buildSearchEngineEndpoint = serviceUrl + "/buildsearchengine";
        restTemplate.getForObject(buildSearchEngineEndpoint, String.class);
    }

    @Scheduled(cron = "0 0 2 * * SUN") // Every Sunday at 2 am
    public void runIndeedCrawler() {
        // Call /crawlIndeed endpoint
        String crawlIndeedEndpoint = serviceUrl + "/crawlIndeed";
        restTemplate.getForObject(crawlIndeedEndpoint, String.class);

        // Call /buildsearchengine endpoint
        String buildSearchEngineEndpoint = serviceUrl + "/buildsearchengine";
        restTemplate.getForObject(buildSearchEngineEndpoint, String.class);
    }

    @Scheduled(cron = "0 0 3 * * SUN") // Every Sunday at 3 am
    public void runWorkopolisCrawler() {
        // Call /crawlWorkopolis endpoint
        String crawlWorkopolisEndpoint = serviceUrl + "/crawlWorkopolis";
        restTemplate.getForObject(crawlWorkopolisEndpoint, String.class);

        // Call /buildsearchengine endpoint
        String buildSearchEngineEndpoint = serviceUrl + "/buildsearchengine";
        restTemplate.getForObject(buildSearchEngineEndpoint, String.class);
    }
}
