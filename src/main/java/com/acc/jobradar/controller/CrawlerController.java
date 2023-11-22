package com.acc.jobradar.controller;

import com.acc.jobradar.crawler.IndeedCrawler;
import com.acc.jobradar.crawler.LinkedinCrawler;
import com.acc.jobradar.crawler.WorkopolisCrawler;
import com.acc.jobradar.database.Database;
import com.acc.jobradar.invertedindex.InvertedIndex;
import com.acc.jobradar.model.JobPosting;
import com.acc.jobradar.parser.HtmlParserIndeed;
import com.acc.jobradar.parser.HtmlParserLinkedin;
import com.acc.jobradar.parser.HtmlParserWorkopolis;
import com.acc.jobradar.vocabulary.VocabularyBuilder;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class CrawlerController {
    private LinkedinCrawler linkedinCrawler;
    private IndeedCrawler indeedCrawler;
    private WorkopolisCrawler workopolisCrawler;

    @GetMapping("/crawlLinkedin")
    public String crawlLinkedin() throws InterruptedException {
        linkedinCrawler.getJobPosting("co-op","Canada");
        return "Done crawling linkedin";
    }
    @GetMapping("/crawlIndeed")
    public String crawlIndeed() throws InterruptedException {
        indeedCrawler.getJobPosting("co-op","Canada");
        return "Done crawling Indeed";
    }
    @GetMapping("/crawlWorkopolis")
    public String crawlWorkopolis() throws InterruptedException {
        workopolisCrawler.getJobPosting("co-op","Canada");
        return "Done crawling Workopolis";
    }

    @GetMapping("/crawl")
    public String crawl() throws InterruptedException {
        linkedinCrawler.getJobPosting("co-op","Canada");
        System.out.println("Done crawling Linkedin");

        indeedCrawler.getJobPosting("co-op","Canada");
        System.out.println("Done crawling Indeed");

        workopolisCrawler.getJobPosting("co-op","Canada");
        System.out.println("Done crawling Workopolis");

        return "Done crawling all the websites";
    }
}
