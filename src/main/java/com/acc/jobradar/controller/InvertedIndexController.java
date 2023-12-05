package com.acc.jobradar.controller;

import com.acc.jobradar.database.Database;
import com.acc.jobradar.invertedindex.InvertedIndex;
import com.acc.jobradar.model.JobPosting;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class InvertedIndexController {
    private final InvertedIndex invertedIndex;
    private final Database database;

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
}
