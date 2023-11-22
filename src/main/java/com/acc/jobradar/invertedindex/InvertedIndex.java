package com.acc.jobradar.invertedindex;

import com.acc.jobradar.model.JobPosting;
import com.acc.jobradar.textparser.TextParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class TrieNode {
    Map<Character, TrieNode> children = new HashMap<>();
    List<String> jobIds = new ArrayList<>();
}

@Service
@RequiredArgsConstructor
public class InvertedIndex {
    private TrieNode root = new TrieNode();
    private final TextParser textParser;

    public void buildIndex(List<JobPosting> jobPostings) {
        for (JobPosting jobPosting : jobPostings) {
            insertWords(jobPosting.getJobId(), jobPosting.getJobTitle());
            insertWords(jobPosting.getJobId(), jobPosting.getCompany());
            insertWords(jobPosting.getJobId(), jobPosting.getLocation());
            insertWords(jobPosting.getJobId(), jobPosting.getDescription());
        }
    }

    private void insertWords(String jobId, String text) {
        List<String> words = textParser.extractWords(text);
        for (String word : words) {
            insert(word.toLowerCase(), jobId);
        }
    }

    private void insert(String word, String jobId) {
        TrieNode node = root;
        for (char ch : word.toCharArray()) {
            node.children.putIfAbsent(ch, new TrieNode());
            node = node.children.get(ch);
        }
        node.jobIds.add(jobId);  // Add jobId only at the end of the word
    }

    public List<String> search(String word) {
        TrieNode node = root;
        for (char ch : word.toLowerCase().toCharArray()) {
            if (!node.children.containsKey(ch)) {
                return new ArrayList<>(); // No postings for this word
            }
            node = node.children.get(ch);
        }
        return node.jobIds;
    }
}

