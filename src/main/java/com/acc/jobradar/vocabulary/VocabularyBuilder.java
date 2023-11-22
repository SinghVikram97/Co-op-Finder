package com.acc.jobradar.vocabulary;

import com.acc.jobradar.database.Database;
import com.acc.jobradar.model.JobPosting;
import com.acc.jobradar.textparser.TextParser;
import lombok.AllArgsConstructor;
import org.checkerframework.checker.units.qual.A;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class VocabularyBuilder {
    private final TextParser textParser;
    private final Database database;

    public void buildVocabulary(List<JobPosting> jobPostingList) {
        for (JobPosting jobPosting : jobPostingList) {
            database.addWordsToVocabulary(textParser.extractWords(jobPosting.getJobTitle()));
            database.addWordsToVocabulary(textParser.extractWords(jobPosting.getLocation()));
            database.addWordsToVocabulary(textParser.extractWords(jobPosting.getCompany()));
            database.addWordsToVocabulary(textParser.extractWords(jobPosting.getDescription()));
        }
    }
}
