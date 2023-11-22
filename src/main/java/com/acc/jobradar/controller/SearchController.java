package com.acc.jobradar.controller;

import com.acc.jobradar.database.Database;
import com.acc.jobradar.invertedindex.InvertedIndex;
import com.acc.jobradar.model.JobPosting;
import com.acc.jobradar.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;
    private final InvertedIndex invertedIndex;
    private final Database database;

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

    @GetMapping("/invertedIndex/{userInput}")
    public ResponseEntity<List<JobPosting>> searchJobPosting(@PathVariable String userInput) {
        List<String> jobPostingIds = invertedIndex.search(userInput);
        List<JobPosting> result = new ArrayList<>();
        jobPostingIds.forEach(jobId -> {
            Optional<JobPosting> jobPosting = database.getJobPosting(jobId);
            jobPosting.ifPresent(result::add);
        });
        return ResponseEntity.ok(result);
    }

    @GetMapping("/vocab")
    public ResponseEntity<Set<String>> getVocab(){
        return ResponseEntity.ok(database.getVocabulary());
    }
}
