package com.acc.jobradar.controller;

import com.acc.jobradar.database.Database;
import com.acc.jobradar.model.JobPosting;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class JobController {
    private final Database database;
    @GetMapping("/jobPosting/{jobId}")
    public ResponseEntity<JobPosting> getJobPosting(@PathVariable String jobId) {
        Optional<JobPosting> jobPostingOptional = database.getJobPosting(jobId);
        if(jobPostingOptional.isPresent()){
            JobPosting jobPosting = jobPostingOptional.get();
            return ResponseEntity.ok(jobPosting);
        }
        else{
            return ResponseEntity.ok(null);
        }
    }
}
