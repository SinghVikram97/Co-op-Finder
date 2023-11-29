package com.acc.jobradar.pageranking;

import com.acc.jobradar.frequencycounter.FrequencyCounter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PageRanking {
    private final FrequencyCounter frequencyCounter;

    // Returns sorted list of jobIds sorted by rank
    public List<String> getPageRanks(String searchString) {
        Map<String, Integer> totalFrequencyMap = new HashMap<>();

        String[] wordsInSearchString = searchString.split("\\s");

        // get frequency of each word in job postings database
        for (String wordInSearchString : wordsInSearchString) {
            Map<String, Integer> wordFrequency = frequencyCounter.getWordFrequency(wordInSearchString);

            for (Map.Entry<String, Integer> entry : wordFrequency.entrySet()) {
                String jobId = entry.getKey();
                int frequency = entry.getValue();
                totalFrequencyMap.put(jobId, totalFrequencyMap.getOrDefault(jobId, 0) + frequency);
            }
        }

        // Sort the job IDs based on their total frequency
        List<String> sortedJobIds = new ArrayList<>(totalFrequencyMap.keySet());
        sortedJobIds.sort((jobId1, jobId2) -> Integer.compare(totalFrequencyMap.get(jobId2), totalFrequencyMap.get(jobId1)));

        // Return the sorted list of job IDs
        return sortedJobIds;
    }
}
