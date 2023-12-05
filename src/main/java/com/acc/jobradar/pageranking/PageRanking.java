package com.acc.jobradar.pageranking;

import com.acc.jobradar.frequencycounter.FrequencyCounter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

@Service
@RequiredArgsConstructor
public class PageRanking {
    private final FrequencyCounter frequencyCounter;

    // Custom Pair class
    @Getter
    private static class JobFrequencyPair {
        String job;
        int frequency;

        public JobFrequencyPair(String job, int frequency) {
            this.job = job;
            this.frequency = frequency;
        }
    }


    public List<String> getPageRanks(String searchString) {
        // Map to store the total frequency of each job ID
        Map<String, Integer> totalFrequencyMap = new HashMap<>();

        // Split the search string into individual words
        String[] wordsInSearchString = searchString.split("\\s");

        // Use a priority queue to sort job frequencies based on their values (in descending order)
        PriorityQueue<JobFrequencyPair> priorityQueue =
                new PriorityQueue<>(Comparator.comparingInt(JobFrequencyPair::getFrequency).reversed());
        try {
            // Calculating the frequency of each word in job postings database
            for (String wordInSearchString : wordsInSearchString) {
                Map<String, Integer> wordFrequency = frequencyCounter.getWordFrequency(wordInSearchString);

                // Updating the total frequency map with the total frequency for each job ID
                for (Map.Entry<String, Integer> entry : wordFrequency.entrySet()) {
                    String jobId = entry.getKey();
                    int frequency = entry.getValue();
                    totalFrequencyMap.put(jobId, totalFrequencyMap.getOrDefault(jobId, 0) + frequency);
                }
            }

            // Populate the priority queue with entries from totalFrequencyMap
            for (Map.Entry<String, Integer> entry : totalFrequencyMap.entrySet()) {
                priorityQueue.offer(new JobFrequencyPair(entry.getKey(), entry.getValue()));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        // Extracting sorted job IDs from the priority queue to list
        List<String> sortedJobIds = new ArrayList<>();
        while (!priorityQueue.isEmpty()) {
            sortedJobIds.add(priorityQueue.poll().getJob());
        }
        // Return the sorted list of job IDs
        return sortedJobIds;
    }
}