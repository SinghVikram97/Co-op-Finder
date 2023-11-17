package com.acc.jobradar.controller;

import com.acc.jobradar.model.JobPosting;
import com.acc.jobradar.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;

    @GetMapping("/suggest/{userInput}")
    public ResponseEntity<List<String>> suggestWords(@PathVariable String userInput) {
        List<String> suggestions = searchService.getSuggestions(userInput);
        return ResponseEntity.ok(suggestions);
    }

    @GetMapping("/jobposting/{userInput}")
    public ResponseEntity<List<JobPosting>> searchDocuments(@PathVariable String userInput) {
        List<JobPosting> jobPostings = searchService.searchJobPosting(userInput);
        return ResponseEntity.ok(jobPostings);
    }
}
