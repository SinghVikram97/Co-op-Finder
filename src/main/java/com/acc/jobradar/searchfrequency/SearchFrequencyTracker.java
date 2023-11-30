package com.acc.jobradar.searchfrequency;

import com.acc.jobradar.database.Database;
import com.acc.jobradar.textparser.TextParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SearchFrequencyTracker {
    private final Database database;
    private final TextParser textParser;

    public void recordSearch(String userInput) {
        try {
            // Remove unnecessary whitespaces and tokenize the input
            String strWithoutWhiteSpace = userInput.replaceAll("\\s", "");
            if (strWithoutWhiteSpace.isEmpty()) {
                throw new IllegalArgumentException("User Input string must not be empty");
            }

            String[] tokens = userInput.trim().toLowerCase().split("\\s+");

            // Remove punctuation
            List<String> wordsWithoutPunctuation = Arrays.stream(tokens)
                    .map(token -> token.replaceAll("[^a-zA-Z]", ""))
                    .toList();

            // Remove stop words
            List<String> wordsWithoutStopWords = textParser.removeCommonEnglishStopWords(wordsWithoutPunctuation);

            // Concatenate the tokens to get the standardized search term
            String standardizedTerm = String.join(" ", wordsWithoutStopWords);

            // Update frequency of the standardized search term
            database.addSearchTermToTermFrequencyMap(standardizedTerm);

            // Update the search frequency for each individual word
            database.addSearchWordsToWordFrequencyMap(wordsWithoutStopWords);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public Map<String, Integer> getTrendingTerms() {
        return sortByValueDescending(database.getTermFrequencyMap());
    }

    // Method to get overall word frequencies
    public Map<String, Integer> getTrendingWords() {
        return sortByValueDescending(database.getWordFrequencyMap());
    }

    // Helper method to sort a map by values in descending order
    private <K, V extends Comparable<? super V>> Map<K, V> sortByValueDescending(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
        list.sort(Collections.reverseOrder(Map.Entry.comparingByValue()));

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }


}
