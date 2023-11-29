package com.acc.jobradar.frequencycounter;

import com.acc.jobradar.database.Database;
import com.acc.jobradar.invertedindex.InvertedIndex;
import com.acc.jobradar.model.JobPosting;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FrequencyCounter {
    private final InvertedIndex invertedIndex;
    private final Database database;

    // Returns a map of jobId, frequency of given word
    public Map<String,Integer> getWordFrequency(String word) {
        // Create a map of job id , frequency
        HashMap<String, Integer> jobIdFrequencyMap = new HashMap<>();
        // Get job ids containing the given word
        List<String> jobIdList = invertedIndex.search(word);
        // For each job calculate frequency of this word

        for (String jobId : jobIdList) {
            int wordFrequency=0;
            Optional<JobPosting> jobPostingOptional = database.getJobPosting(jobId);
            if(jobPostingOptional.isPresent()){
                JobPosting jobPosting = jobPostingOptional.get();

                // Calculate frequency of word in given job posting
                wordFrequency = getWordFrequencyInJobPosting(jobPosting, word);
            }

            // Insert into map
            jobIdFrequencyMap.put(jobId, wordFrequency);
        }
        return jobIdFrequencyMap;
    }

    private int getWordFrequencyInJobPosting(JobPosting jobPosting, String word) {
        int frequency=0;

        String jobTitle = jobPosting.getJobTitle();
        String description = jobPosting.getDescription();
        String company = jobPosting.getCompany();
        String location = jobPosting.getLocation();

        String joinedString = String.join(" ", jobTitle, description, company, location);

        String[] wordsInJobPosting = joinedString.split("\\s");

        for (String wordInJobPosting : wordsInJobPosting) {
            if(word.equals(wordInJobPosting)){
                frequency++;
            }
        }
        return frequency;
    }

}
