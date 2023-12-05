package com.acc.jobradar.controller;

import com.acc.jobradar.autocomplete.AutoComplete;
import com.acc.jobradar.database.Database;
import com.acc.jobradar.invertedindex.InvertedIndex;
import com.acc.jobradar.model.JobPosting;
import com.acc.jobradar.parser.HtmlParserIndeed;
import com.acc.jobradar.parser.HtmlParserLinkedin;
import com.acc.jobradar.parser.HtmlParserWorkopolis;
import com.acc.jobradar.vocabulary.VocabularyBuilder;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private AutoComplete autoComplete;
    private final Logger logger = LoggerFactory.getLogger(BuildSearchEngine.class);

    @GetMapping("/buildsearchengine")
    public String buildSearchEngine() {
        logger.info("Starting parsing of Linkedin HTML Files");
        List<JobPosting> jobPostingsFromLinkedin = htmlParserLinkedin.parseLinkedinHtmlFiles();
        logger.info("Done parsing Linkedin HTML Files");

        logger.info("Starting parsing of Indeed HTML Files");
        List<JobPosting> jobPostingsFromIndeed = htmlParserIndeed.parseIndeedHtmlFiles();
        logger.info("Done parsing Indeed HTML Files");

        logger.info("Starting parsing of Workopolis HTML Files");
        List<JobPosting> jobPostingsFromWorkopolis = htmlParserWorkopolis.parseWorkopolisHtmlFiles();
        logger.info("Done parsing Workopolis HTML Files");

        logger.info("Adding words to Vocabulary");
        vocabularyBuilder.buildVocabulary(jobPostingsFromLinkedin);
        vocabularyBuilder.buildVocabulary(jobPostingsFromWorkopolis);
        vocabularyBuilder.buildVocabulary(jobPostingsFromIndeed);
        logger.info("Done adding words to Vocabulary");

        logger.info("Adding job postings to database");
        database.addJobPostings(jobPostingsFromLinkedin);
        database.addJobPostings(jobPostingsFromIndeed);
        database.addJobPostings(jobPostingsFromWorkopolis);
        logger.info("Finished adding job postings to database");

        logger.info("Building inverted index from job postings");
        invertedIndex.buildIndex(database.getJobPostings());
        logger.info("Finished building inverted index from job postings");

        logger.info("Finished building inverted index from job postings");

        logger.info("Building autocomplete data structure from job postings");
        autoComplete.buildAutoComplete(database.getJobPostings());
        logger.info("Finished building autocomplete data structure from job postings");

        logger.info("Search Engine Built and Updated");
        return "Search Engine built and updated";
    }
}
