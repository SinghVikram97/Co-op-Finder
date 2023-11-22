package com.acc.jobradar.controller;

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
public class BuildSearchEngine {
    private HtmlParserLinkedin htmlParserLinkedin;
    private HtmlParserIndeed htmlParserIndeed;
    private HtmlParserWorkopolis htmlParserWorkopolis;
    private Database database;
    private InvertedIndex invertedIndex;
    private VocabularyBuilder vocabularyBuilder;

    @GetMapping("/buildsearchengine")
    public String buildSearchEngine() {
        List<JobPosting> jobPostingsFromLinkedin = htmlParserLinkedin.parseLinkedinHtmlFiles();
        System.out.println("Done parsing Linkedin");

        List<JobPosting> jobPostingsFromIndeed = htmlParserIndeed.parseIndeedHtmlFiles();
        System.out.println("Done parsing Indeed");

        List<JobPosting> jobPostingsFromWorkopolis = htmlParserWorkopolis.parseWorkopolisHtmlFiles();
        System.out.println("Done parsing Workopolis");

        vocabularyBuilder.buildVocabulary(jobPostingsFromLinkedin);
        vocabularyBuilder.buildVocabulary(jobPostingsFromWorkopolis);
        vocabularyBuilder.buildVocabulary(jobPostingsFromIndeed);

        database.addJobPostings(jobPostingsFromLinkedin);
        database.addJobPostings(jobPostingsFromIndeed);
        database.addJobPostings(jobPostingsFromWorkopolis);

        invertedIndex.buildIndex(database.getJobPostings());
        return "Built vocabulary and inverted index";
    }
}
