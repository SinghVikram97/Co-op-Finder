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
import java.util.Objects;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class Database {
    private final Logger logger = LoggerFactory.getLogger(Database.class);

    // List of Job Posting
    private static final ArrayList<JobPosting> jobPostingList = new ArrayList<>();
    // Vocabulary Set
    private static final Set<String> vocabularySet = new HashSet<>();

    // Map to store the searched words and their frequencies.
    private final Map<String, Integer> wordFrequencyMap = new HashMap<>();
    // Map and maxHeap Data structure to store the top 10 searched terms.
    private final Map<String, Integer> termFrequencyMap = new HashMap<>();
    private final PriorityQueue<String> maxHeap = new PriorityQueue<>((term1, term2) -> Objects.equals(termFrequencyMap.get(term2), termFrequencyMap.get(term1)) ? term1.compareTo(term2) : termFrequencyMap.get(term2) - termFrequencyMap.get(term1));

    private static List<String> commonEnglishStopWords = new ArrayList<>();

    {
        try{
            commonEnglishStopWords = FileHandler.getFileContents("common-stop-words.txt");
        }catch (Exception exception) {
            logger.error("Error while getting stop words cause:{} message:{}", exception.getCause(), exception.getMessage());
        }
    }

    // Add list of job postings in database
    public void addJobPostings(List<JobPosting> jobPostings) {
        jobPostingList.addAll(jobPostings);
    }

    // Get all job postings from database
    public List<JobPosting>  getJobPostings(){
        return jobPostingList;
    }

    // Get JobPosting by its jobId

    public Optional<JobPosting> getJobPosting(String jobId) {
        return jobPostingList.stream().filter(jobPosting -> jobPosting.getJobId().equals(jobId)).findFirst();
    }

    // Add list of words in the vocabulary set
    public void addWordsToVocabulary(List<String> words) {
        vocabularySet.addAll(words);
    }

    // Get vocabulary from database
    public Set<String> getVocabulary() {
        return Collections.unmodifiableSet(vocabularySet);
    }

    public void addSearchTermToTermFrequencyMap(String searchTerm) {
        // Update the search freq in term Map.
        termFrequencyMap.put(searchTerm, termFrequencyMap.getOrDefault(searchTerm, 0) + 1);
        // Clear the heap so we can store the updated freq data.
        maxHeap.clear();
        // Add each string with updated freq in the heap.
        for(String s : termFrequencyMap.keySet()) maxHeap.offer(s);
        // Only Store the top 10 Elements in the maxHeap
        if (maxHeap.size() > 10) {
            maxHeap.poll();
        }
    }

    // Get top 10 Searched terms from heap.
    public String[] getTermFrequencyMap() {
        return maxHeap.toArray(new String[0]);
    }

    // Add list of words from searched term in the map
    public void addSearchWordsToWordFrequencyMap(List<String> words) {
        for(String word : words) {
            wordFrequencyMap.put(word, wordFrequencyMap.getOrDefault(word, 0) + 1);
        }
    }

    // Return the freq of words along with the words in map.
    public Map<String, Integer> getWordFrequencyMap() {
        return Collections.unmodifiableMap(wordFrequencyMap);
    }

    public List<String> getCommonEnglishStopWords() {
        return commonEnglishStopWords;
    }
}
