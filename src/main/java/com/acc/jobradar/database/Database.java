package com.acc.jobradar.database;

import com.acc.jobradar.filehandler.FileHandler;
import com.acc.jobradar.model.JobPosting;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class Database {
    private final Logger logger = LoggerFactory.getLogger(Database.class);
    private static final ArrayList<JobPosting> jobPostingList = new ArrayList<>();
    private static final Set<String> vocabularySet = new HashSet<>();

    private final Map<String, Integer> wordFrequencyMap = new HashMap<>();
    private final Map<String, Integer> termFrequencyMap = new HashMap<>();

    private static List<String> commonEnglishStopWords = new ArrayList<>();

    {
        try{
            commonEnglishStopWords = FileHandler.getFileContents("common-stop-words.txt");
        }catch (Exception exception) {
            logger.error("Error while getting stop words cause:{} message:{}", exception.getCause(), exception.getMessage());
        }
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
    public void addWordsToVocabulary(List<String> words) {
        vocabularySet.addAll(words);
    }

    public Set<String> getVocabulary() {
        return Collections.unmodifiableSet(vocabularySet);
    }

    public void addSearchTermToTermFrequencyMap(String searchTerm) {
        termFrequencyMap.put(searchTerm, termFrequencyMap.getOrDefault(searchTerm, 0) + 1);
    }

    public Map<String, Integer> getTermFrequencyMap() {
        return Collections.unmodifiableMap(termFrequencyMap);
    }

    public void addSearchWordsToWordFrequencyMap(List<String> words) {
        for(String word : words) {
            wordFrequencyMap.put(word, wordFrequencyMap.getOrDefault(word, 0) + 1);
        }
    }

    public Map<String, Integer> getWordFrequencyMap() {
        return Collections.unmodifiableMap(wordFrequencyMap);
    }

    public List<String> getCommonEnglishStopWords() {
        return commonEnglishStopWords;
    }
}
