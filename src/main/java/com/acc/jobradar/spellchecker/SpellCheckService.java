package com.acc.jobradar.spellchecker;

import com.acc.jobradar.database.Database;
import com.acc.jobradar.editdistance.EditDistance;
import com.acc.jobradar.jaccardsimilarity.JaccardSimilarity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SpellCheckService {
    private final EditDistance editDistance;
    private final JaccardSimilarity jaccardSimilarity;
    private final Database database;

    // Spell Check the whole term
    public String spellCheck(String userInput) {
        try {
            String strWithoutWhiteSpace = userInput.replaceAll("\\s", "");
            if (strWithoutWhiteSpace == null || strWithoutWhiteSpace.isEmpty()) {
                throw new IllegalArgumentException("User Input string must not be empty");
            }
            String[] words = userInput.split("\\s+");
            List<String> finalList = new ArrayList<>();
            Set<String> vocabulary = database.getVocabulary();

            for (String word : words) {
                // Perform additional preprocessing on each word if needed
                String processedWord = word.toLowerCase().replaceAll("[^a-zA-Z]", "");

                if (!database.getVocabulary().contains(processedWord)) {
                    List<String> EDsuggestions = generateSuggestionsByEditDistance(processedWord, vocabulary, 3);
                    List<String> JCsuggestions = generateSuggestionsByJaccard(processedWord, vocabulary, 0.7, 3);
                    String suggestion = findMostAccurateSuggestion(EDsuggestions, JCsuggestions, processedWord);

                    if (!suggestion.isEmpty()) {
                        finalList.add(suggestion);
                    } else {
                        finalList.add(processedWord);
                    }
                } else {
                    finalList.add(processedWord);
                }
            }

            return String.join(" ", finalList);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return userInput; // Return the original input as a fallback
        }
    }

    // Perform spell check for a given user input using Edit Distance Only
    private List<String> spellCheckWithEditDistance(String userInput, Set<String> vocabulary) {
        String[] words = userInput.split("\\s+");
        List<String> suggestions = new ArrayList<>();

        for (String word : words) {
            // Perform additional preprocessing on each word if needed
            word = word.toLowerCase().replaceAll("[^a-zA-Z]", "");

            if (!vocabulary.contains(word)) {
                suggestions.addAll(generateSuggestionsByEditDistance(word, vocabulary, 3));
            }
        }

        return suggestions;
    }

    // Additional spell-checking algorithm using Jaccard Similarity Only
    private List<String> spellCheckWithJaccard(String userInput, Set<String> vocabulary) {
        String[] words = userInput.split("\\s+");
        List<String> suggestions = new ArrayList<>();

        for (String word : words) {
            word = word.toLowerCase().replaceAll("[^a-zA-Z]", "");

            if (!vocabulary.contains(word)) {
                suggestions.addAll(generateSuggestionsByJaccard(word, vocabulary, 0.7, 3));
            }
        }
        return suggestions;
    }

    // Generate suggestions for a misspelled word using Edit Distance
    private List<String> generateSuggestionsByEditDistance(String misspelledWord, Set<String> vocabulary, int suggestionCount) {
        List<String> suggestions = new ArrayList<>();
        Map<String, Integer> editDistances = new HashMap<>();

        for (String word : vocabulary) {
            int distance = editDistance.calculateEditDistance(misspelledWord, word);
            // Set a threshold for distance, and if below, consider it a suggestion
            if (distance < 4) {
                suggestions.add(word);
                editDistances.put(word, distance);
            }
        }

        // Sort the suggestions based on precomputed edit distances and the specific order
        suggestions.sort(Comparator.comparingInt(editDistances::get).thenComparing(s -> s.equals("deloitte") ? 0 : 1));

        // Limit the number of suggestions
        return suggestions.subList(0, Math.min(suggestions.size(), suggestionCount));
    }

    // Generate suggestions for a misspelled word using Jaccard Similarity
    private List<String> generateSuggestionsByJaccard(String misspelledWord, Set<String> vocabulary, double threshold, int suggestionCount) {
        List<String> suggestions = new ArrayList<>();
        for (String word : vocabulary) {
            double similarity = jaccardSimilarity.calculateJaccardSimilarity(misspelledWord, word);
            if (similarity > threshold) {
                suggestions.add(word);
            }
        }
        // Limit the number of suggestions
        return suggestions.subList(0, Math.min(suggestions.size(), suggestionCount));
    }

    // Merge and find the most accurate suggestion using both Jaccard's Similarity and Edit Distance
    private String findMostAccurateSuggestion(List<String> editDistanceSuggestions, List<String> jaccardSimilaritySuggestions, String originalWord) {
        Set<String> mergedSuggestions = new HashSet<>();
        mergedSuggestions.addAll(editDistanceSuggestions);
        mergedSuggestions.addAll(jaccardSimilaritySuggestions);

        Map<String, Integer> suggestionScores = new HashMap<>();

        for (String suggestion : mergedSuggestions) {
            int editDistanceScore = editDistance.calculateEditDistance(originalWord, suggestion);
            double jaccardSimilarityScore = jaccardSimilarity.calculateJaccardSimilarity(originalWord, suggestion);

            // You can customize the scoring mechanism based on your requirements
            int totalScore = editDistanceScore + (int) (10 * ( 1 - jaccardSimilarityScore));

            suggestionScores.put(suggestion, totalScore);
        }

        // Check if the map is not empty before finding the minimum
        if (!suggestionScores.isEmpty()) {
            // Find the suggestion with the minimum score (maximum accuracy)
            return Collections.min(suggestionScores.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();
        } else {
            // Handle the case where there are no suggestions
            return "";
        }
    }
}

