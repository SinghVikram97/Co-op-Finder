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

    // returns a map of jobId and frequency of the given word in job postings.
    public Map<String, Integer> getWordFrequency(String word) {

        // Creating a map of job id , frequency
        HashMap<String, Integer> jobIdFrequencyMap = new HashMap<>();
        try {
            // Get job ids containing the given word
            List<String> jobIdList = invertedIndex.search(word);

            // For each job calculate frequency of this word
            for (String jobId : jobIdList) {
                int wordFrequency = 0;
                Optional<JobPosting> jobPostingOptional = database.getJobPosting(jobId);

                // Retrieve the JobPosting for the given jobId
                if (jobPostingOptional.isPresent()) {
                    JobPosting jobPosting = jobPostingOptional.get();

                    // Calculate frequency of word in given job posting
                    wordFrequency = getWordFrequencyInJobPosting(jobPosting, word);
                }

                // Insert into map
                jobIdFrequencyMap.put(jobId, wordFrequency);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jobIdFrequencyMap;
    }

    private int getWordFrequencyInJobPosting(JobPosting jobPosting, String word) {
        int frequency = 0;
        try {
            // Converting job attributes to lowercase for case-insensitive comparison
            String jobTitle = jobPosting.getJobTitle().toLowerCase();
            String description = jobPosting.getDescription().toLowerCase();
            ;
            String company = jobPosting.getCompany().toLowerCase();
            ;
            String location = jobPosting.getLocation().toLowerCase();
            ;

            // Combine job attributes into a single string
            String joinedString = String.join(" ", jobTitle, description, company, location);

            // Split the joined string into individual words
            String[] wordsInJobPosting = joinedString.split("\\s");

            // Count the frequency of the given word in the JobPosting
            for (String wordInJobPosting : wordsInJobPosting) {
                if (word.equals(wordInJobPosting)) {
                    frequency++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return frequency;
    }

}
