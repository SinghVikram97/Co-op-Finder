package com.acc.jobradar.frequencycounter;

import com.acc.jobradar.database.Database;
import com.acc.jobradar.invertedindex.InvertedIndex;
import com.acc.jobradar.model.JobIDFrequency;
import com.acc.jobradar.model.JobPosting;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

//@Service
//@RequiredArgsConstructor
//public class FrequencyCounter {
//    private final InvertedIndex invertedIndex;
//    private final Database database;
//
//    public void addWordsToWordFrequencyMap() {
//        database.getJobPostings().forEach(jobPosting -> {
//            String jobDetails = jobPosting.getJobTitle() + jobPosting.getDescription() + jobPosting.getCompany() + jobPosting.getLocation();
//
//            String[] words = jobDetails.split("\\s+");
//
//            for (String word : words) {
//                database.addWordToWordFrequencyMap(word, jobPosting.getJobId());
//            }
//        });
//
//    }
//}
