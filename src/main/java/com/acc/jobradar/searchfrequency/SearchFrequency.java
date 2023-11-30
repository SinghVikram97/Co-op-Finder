package com.acc.jobradar.searchfrequency;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
public class SearchFrequency {
    private Map<String, Integer> wordFrequencyMap;
    private Map<String, Integer> termFrequencyMap;
}
