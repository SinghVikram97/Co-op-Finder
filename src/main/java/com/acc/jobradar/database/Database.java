package com.acc.jobradar.database;

import com.acc.jobradar.model.JobIDFrequency;
import com.acc.jobradar.model.JobPosting;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class Database {
    private static final ArrayList<JobPosting> jobPostingList = new ArrayList<>();
    private static final Set<String> vocabularySet = new LinkedHashSet<>();

    private static final Map<String, List<JobIDFrequency>> wordFrequencyMap = new HashMap<>();

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
        vocabularySet.add(word);
    }

    public void addWordsToVocabulary(List<String> words) {
        vocabularySet.addAll(words);
    }

    public Set<String> getVocabulary() {
        return Collections.unmodifiableSet(vocabularySet);
    }

//    public void addWordToWordFrequencyMap(String word, String jobId) {
//        wordFrequencyMap.put(word, wordFrequencyMap.getOrDefault(word, JO) + 1);
//        HashJobIDCount.put(subJobId, HashJobIDCount.getOrDefault(subJobId, 0) + frequency);
//    }
}
