package com.acc.jobradar.service;

import com.acc.jobradar.database.Database;
import com.acc.jobradar.model.JobPosting;
import com.acc.jobradar.pageranking.PageRanking;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final PageRanking pageRanking;
    private final Database database;

    public List<JobPosting> searchJobPosting(String userInput) {
        // Returns list of jobIds sorted by ranks
        List<String> jobIdsSortedByRank = pageRanking.getPageRanks(userInput);
        List<JobPosting> searchResults = new ArrayList<>();
        for (String jobId : jobIdsSortedByRank) {
            Optional<JobPosting> jobPostingOptional = database.getJobPosting(jobId);
            jobPostingOptional.ifPresent(searchResults::add);
        }
        return searchResults;
    }
}
