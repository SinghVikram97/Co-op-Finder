package com.acc.jobradar.database;

import com.acc.jobradar.model.JobPosting;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class Database {
    private static final ArrayList<JobPosting> jobPostingList = new ArrayList<>();
    private static final Set<String> vocabularySet = new LinkedHashSet<>();
    public void addJobPostings(List<JobPosting> jobPostings) {
        jobPostingList.addAll(jobPostings);
    }

    public List<JobPosting>  getJobPostings(){
        return jobPostingList;
    }

    public Optional<JobPosting> getJobPosting(String jobId) {
        return jobPostingList.stream().filter(jobPosting -> jobPosting.getJobId().equals(jobId)).findFirst();
    }
    public void addWordsToVocabulary(List<String> words) {
        vocabularySet.addAll(words);
    }

    public Set<String> getVocabulary() {
        return Collections.unmodifiableSet(vocabularySet);
    }
}
