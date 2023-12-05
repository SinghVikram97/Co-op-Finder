package com.acc.jobradar.searchfrequency;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * Represents the search frequency data, including word frequencies and term frequencies.
 */
@Setter
@Getter
public class SearchFrequency {
    // Map to store word frequencies
    private Map<String, Integer> wordFrequencyMap;

    // Array to store term frequencies
    private String[] termFrequencyMap;
}
