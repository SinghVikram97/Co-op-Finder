package com.acc.jobradar.spellchecker;

import com.acc.jobradar.database.Database;
import com.acc.jobradar.editdistance.EditDistance;
import com.acc.jobradar.jaccardsimilarity.JaccardSimilarity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.acc.jobradar.filehandler.FileHandler;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SpellCheckService {
    private final EditDistance editDistance;
    private final JaccardSimilarity jaccardSimilarity;
    private final Database database;

    private static final Map<String, Integer> editDistances = new HashMap<>();
    private static final Map<String, Double> jaccardSimilarityScores = new HashMap<>();

    // Ignore common stop word spelling
    public Boolean isInCommonStopWordList(String word){
        try {
            List<String> commonEnglishStopWords = database.getCommonEnglishStopWords();
            return commonEnglishStopWords.contains(word);
        }catch(Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }


    // Spell Check the whole term
    public String spellCheck(String userInput) {
        try {
            // Replace all whitespace with empty string to check if string is empty or not.
            String strWithoutWhiteSpace = userInput.replaceAll("\\s", "");
            // Raise Error if string is empty.
            if (strWithoutWhiteSpace == null || strWithoutWhiteSpace.isEmpty()) {
                throw new IllegalArgumentException("User Input string must not be empty");
            }

            // Split words using the space
            String[] words = userInput.split("\\s+");

            // List to Store the final Suggestions list
            List<String> finalSuggestionsList = new ArrayList<>();

            // Get Vocabulary from Database
            Set<String> vocabulary = database.getVocabulary();
            for (String word : words) {
                // Perform additional preprocessing on each word if needed
                String processedWord = word.toLowerCase().replaceAll("[^a-zA-Z]", "");

                // Check if processed word exists in the vocabulary. if yes then there is no spelling mistake in the word.
                if (!database.getVocabulary().contains(processedWord) && !isInCommonStopWordList(processedWord)) {
                    // Find all words that have the lowest edit distance.
                    // 3 or less suggestion will be given as per the parameters passed.
                    List<String> EDsuggestions = generateSuggestionsByEditDistance(processedWord, vocabulary, 3);

                    // Find all words that provide the highest similarity score using jaccard's similarity.
                    // Threshold is predefined and passed as arguments 0.7 as Jaccard's similarity is between 0 and 1.
                    //  3 or less suggestion will be given as per the parameters passed.
                    List<String> JCsuggestions = generateSuggestionsByJaccard(processedWord, vocabulary, 0.7, 3);
                    String suggestion = findMostAccurateSuggestion(EDsuggestions, JCsuggestions, processedWord);

                    // if Suggestion is empty, which means there is no suggestion given by both, here add the processed word in the final list.
                    if (!suggestion.isEmpty()) {
                        finalSuggestionsList.add(suggestion);
                    } else {
                        finalSuggestionsList.add(processedWord);
                    }
                }
                else {
                    // No need of spell check, spelling is correct.
                    finalSuggestionsList.add(processedWord);
                }
            }

            // return the joined string of corrected searched term.
            return String.join(" ", finalSuggestionsList);
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
        int lenOfOriginalWord = misspelledWord.length();
        for (String word : vocabulary) {
            int distance = editDistance.calculateEditDistance(misspelledWord, word);
            // Set a threshold for distance, and if below, consider it a suggestion
            if (distance < 4 && Math.abs(word.length() - lenOfOriginalWord) <= 2) {
                suggestions.add(word);
                editDistances.put(word, distance);
            }
        }
        // Sort the suggestions based on precomputed edit distances and the specific order
        suggestions.sort(Comparator.comparingInt(editDistances::get));
        // Limit the number of suggestions
        return suggestions.subList(0, Math.min(suggestions.size(), suggestionCount));
    }

    // Generate suggestions for a misspelled word using Jaccard Similarity
    private List<String> generateSuggestionsByJaccard(String misspelledWord, Set<String> vocabulary, double threshold, int suggestionCount) {
        List<String> suggestions = new ArrayList<>();
        int lenOfOriginalWord = misspelledWord.length();
//        Map<String, Double> jaccardSimilarityScores = new HashMap<>();
        for (String word : vocabulary) {
            double similarity = jaccardSimilarity.calculateJaccardSimilarity(misspelledWord, word);
            if (similarity > threshold && Math.abs(word.length() - lenOfOriginalWord) <= 2) {
                suggestions.add(word);
                jaccardSimilarityScores.put(word,similarity);
            }
        }
        suggestions.sort(Comparator.comparingDouble(jaccardSimilarityScores::get));
        // Limit the number of suggestions
        return suggestions.subList(0, Math.min(suggestions.size(), suggestionCount));
    }

    private String findMostAccurateSuggestion(List<String> editDistanceSuggestions, List<String> jaccardSimilaritySuggestions, String originalWord) {
        Set<String> mergedSuggestions = new HashSet<>(editDistanceSuggestions);
        mergedSuggestions.addAll(jaccardSimilaritySuggestions);
        Map<String, Integer> suggestionScores = new HashMap<>();
        for (String suggestion : mergedSuggestions) {
            // For Each suggestion find out the edit distance and jaccard's similarity score.
            int editDistanceScore = editDistances.containsKey(suggestion)
                    ? editDistances.get(suggestion)
                    : editDistance.calculateEditDistance(originalWord, suggestion);
            double jaccardSimilarityScore = jaccardSimilarityScores.containsKey(suggestion)
                    ? jaccardSimilarityScores.get(suggestion)
                    : jaccardSimilarity.calculateJaccardSimilarity(originalWord, suggestion);

            // Create a normal equation which will provide a combined score for each suggestion
            // Edit distance could be between 1 and 4 so it has just one decimal.
            // Jaccard similarity could be of 0.7 to 1 so to find out the most similar word
            // Subtracting it by 1 so will get the score between 0 and 0.3 and multiplying with 10
            // so will get result between 0 and 3. so it will provide most accurate suggestion.
            int totalScore = editDistanceScore + (int) (10 * (1 - jaccardSimilarityScore));
            int lenOfOriginalWord = originalWord.length();
            if(Math.abs(suggestion.length() - lenOfOriginalWord) <= 2){
                suggestionScores.put(suggestion, totalScore);
            }

        }
        // Clear unnecessary data after spell check
        editDistances.clear();
        jaccardSimilarityScores.clear();
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

