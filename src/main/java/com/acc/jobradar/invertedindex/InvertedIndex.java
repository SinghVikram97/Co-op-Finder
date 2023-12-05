package com.acc.jobradar.invertedindex;

import com.acc.jobradar.controller.BuildSearchEngine;
import com.acc.jobradar.model.JobPosting;
import com.acc.jobradar.textparser.TextParser;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InvertedIndex {
    // Root of the trie
    private final TrieNode invertedIndexRoot;
    private final TextParser textParser;
    private final Logger logger = LoggerFactory.getLogger(InvertedIndex.class);

    // Function to go through list of job postings and add them into our inverted index
    public void buildIndex(List<JobPosting> jobPostings) {
        for (JobPosting jobPosting : jobPostings) {
            // Insert words from different fields of the job posting into the inverted index
            insertWords(jobPosting.getJobId(), jobPosting.getJobTitle());
            insertWords(jobPosting.getJobId(), jobPosting.getCompany());
            insertWords(jobPosting.getJobId(), jobPosting.getLocation());
            insertWords(jobPosting.getJobId(), jobPosting.getDescription());
        }
    }

    // Inserts words from a given text into the trie along with the associated job ID
    private void insertWords(String jobId, String text) {
        // Extract individual words from the text removing the common stop words
        List<String> words = textParser.extractWords(text);
        for (String word : words) {
            // Insert each word into the inverted index trie after converting it to lowercase
            insert(word.toLowerCase(), jobId);
        }
    }


    // Inserts a word and its associated job ID into the inverted index trie
    private void insert(String word, String jobId) {
        TrieNode node = invertedIndexRoot;
        for (char ch : word.toCharArray()) {
            // Create a new TrieNode if the character is not already a child
            node.children.putIfAbsent(ch, new TrieNode());
            // Move to the next node
            node = node.children.get(ch);
            // Add jobId at each character to support partial matches
            node.jobIds.add(jobId);
        }
    }

    // Searches for a word in the inverted index and returns associated job IDs
    public List<String> search(String word) {
        TrieNode node = invertedIndexRoot;
        for (char ch : word.toLowerCase().toCharArray()) {
            // Check if the character is a child in the trie
            if (!node.children.containsKey(ch)) {
                // No postings for this word
                return new ArrayList<>();
            }
            // Move to the next node
            node = node.children.get(ch);
        }
        // Return the list of job IDs associated with the last node in the word
        logger.info("Number of jobIds found for given word: {} is: {}", word, node.jobIds.size());
        return node.jobIds;
    }

}

