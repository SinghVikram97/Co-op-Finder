package com.acc.jobradar.autocomplete;

// Necessary Imports
import com.acc.jobradar.constants.RegexConstants;
import com.acc.jobradar.model.JobPosting;
import com.acc.jobradar.textparser.TextParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.acc.jobradar.constants.RegexConstants.SPACE_REGEX;

// AutoComplete Service class
@Service
@RequiredArgsConstructor
public class AutoComplete {
    private final TrieNode autoCompleteRoot;
    // To extract words, TextParser dependency
    private final TextParser textParser;

    // Building the Trie with all the job postings information
    public void buildAutoComplete(List<JobPosting> jobPostings) {
        for (JobPosting jobPosting : jobPostings) {
            insertWord(jobPosting.getJobTitle());
            insertWord(jobPosting.getCompany());
            insertWord(jobPosting.getLocation());
            insertWord(jobPosting.getDescription());
        }
    }

    // Getting the word suggestions on the basis of the input prefix
    public List<String> suggestWords(String prefix) {
        String lowerCasePrefix = prefix.toLowerCase();

        // Split the input into words and consider the last word as the new prefix
        String[] splitWords = lowerCasePrefix.split(SPACE_REGEX);

        // Extracting the existing words to append to the word completion output
        StringBuilder existingWords = new StringBuilder();
        for (int i = 0; i < splitWords.length - 1; i++) {
            existingWords.append(splitWords[i]).append(" ");
        }

        // For extracting last word as prefix to get all the word completion suggestions
        String latestPrefix = splitWords.length > 0 ? splitWords[splitWords.length - 1] : "";

        List<String> allCompletionsList = getallwordsWithPrefix(latestPrefix, Integer.MAX_VALUE);

        // Preparing the suggestions list
        List<String> wordSuggestions = new ArrayList<>();
        for (String completionOutput : allCompletionsList) {
            wordSuggestions.add(existingWords + completionOutput);
        }

        return wordSuggestions;
    }

    // Getting all words with input prefix and maximum edit distance
    private List<String> getallwordsWithPrefix (String prefix, int editDistancemax) {
        List<String> result = new ArrayList<>();
        TrieNode currentNode = autoCompleteRoot;

        for (char character : prefix.toCharArray()) {
            character = Character.toLowerCase(character); // Convert to lowercase
            if (!currentNode.children.containsKey(character)) {
                return result; // If the words with input Prefix is not found
            }
            currentNode = currentNode.children.get(character);
        }
        // Recursive call
        getAllWordsFromNode(currentNode, prefix, result, editDistancemax);
        return result;
    }

    // To insert word into the Trie
    private void insertWord(String text) {
        List<String> words = textParser.extractWords(text);
        for (String word : words) {
            insert(word.toLowerCase());
        }
    }

    // Insert operation in Trie
    private void insert(String wordString) {
        TrieNode currNode = autoCompleteRoot;                          // Current Node to root
        char[] strToArray = wordString.toCharArray();      // Converting word string to a character Array

        for (char chacter : strToArray) {
            chacter = Character.toLowerCase(chacter); // Convert to lowercase
            currNode.children.putIfAbsent(chacter, new TrieNode());
            currNode = currNode.children.get(chacter);
        }
        currNode.isEndOfWord = true;    // Flag the end of the word
    }

    // to get all words from a given Trie node recursively with a specified prefix and edit distance
    private void getAllWordsFromNode(TrieNode currNode, String currPrefix, List<String> res, int editDistanceMax) {
        // checking if current node is the end og the word,
        if (currNode.isEndOfWord) {
            res.add(currPrefix);         // adding the current prefix to the result list
        }
        // Stop recursion if the edit distance limit is reached. (Base case)
        if (editDistanceMax == 0) {
            return;
        }
        // Traverse the children of the current node (Recursive case)
        for (char ch : currNode.children.keySet()) {
            getAllWordsFromNode(currNode.children.get(ch), currPrefix + ch, res, editDistanceMax - 1);
        }
    }
}
