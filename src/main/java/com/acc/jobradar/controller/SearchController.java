package com.acc.jobradar.controller;

import com.acc.jobradar.model.JobPosting;
import com.acc.jobradar.searchfrequency.SearchFrequencyTracker;
import com.acc.jobradar.service.SearchService;
import com.acc.jobradar.textparser.TextParser;
import com.acc.jobradar.validation.SearchQueryValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;
    private final SearchFrequencyTracker searchFrequencyTracker;
    private final TextParser textParser;

    @GetMapping("/jobposting/{userInput}")
    public ResponseEntity<List<JobPosting>> searchDocuments(@PathVariable String userInput) {
        // remove spaces and covert to lowercase
        userInput = userInput.trim().toLowerCase();
        // check if it is valid input
        if(!SearchQueryValidator.validateString(userInput)){
            return ResponseEntity.badRequest().build();
        }
        // add to search frequency tracker
        searchFrequencyTracker.recordSearch(userInput);

        // remove stop words from user input
        List<String> userInputWithoutStopWords = textParser.extractWords(userInput);

        // join back the user input
        userInput = String.join(" ", userInputWithoutStopWords);

        List<JobPosting> jobPostings = searchService.searchJobPosting(userInput);
        return ResponseEntity.ok(jobPostings);
    }

}
