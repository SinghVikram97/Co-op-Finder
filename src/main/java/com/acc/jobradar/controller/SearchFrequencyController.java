package com.acc.jobradar.controller;

import com.acc.jobradar.searchfrequency.SearchFrequency;
import com.acc.jobradar.searchfrequency.SearchFrequencyTracker;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SearchFrequencyController {
    private final SearchFrequencyTracker searchFrequencyTracker;
    @GetMapping("/searchfrequency/trending")
    public SearchFrequency getTrendingTerms() {
        SearchFrequency searchFrequency = new SearchFrequency();
        searchFrequency.setTermFrequencyMap(searchFrequencyTracker.getTrendingTerms());
        searchFrequency.setWordFrequencyMap(searchFrequencyTracker.getTrendingWords());
        return searchFrequency;
    }

}
