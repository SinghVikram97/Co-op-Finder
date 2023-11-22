package com.acc.jobradar.database;

import com.acc.jobradar.model.JobPosting;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class Database {
    private static final ArrayList<JobPosting> jobPostingList = new ArrayList<>();
    private static final Set<String> vocabulary = new HashSet<>();

    public void addJobPosting(JobPosting jobPosting) {
        jobPostingList.add(jobPosting);
    }

    public void addJobPostings(List<JobPosting> jobPostings) {
        jobPostingList.addAll(jobPostings);
    }

    public List<JobPosting>  getJobPostings(){
        return jobPostingList;
    }

    public Optional<JobPosting> getJobPosting(String jobId) {
        return jobPostingList.stream().filter(jobPosting -> jobPosting.getJobId().equals(jobId)).findFirst();
    }

    public void addWordsToVocabulary(String word) {
        vocabulary.add(word);
    }

    public void addWordsToVocabulary(List<String> words) {
        vocabulary.addAll(words);
    }

    public Set<String> getVocabulary() {
        return Collections.unmodifiableSet(vocabulary);
    }
}
