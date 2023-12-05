package com.acc.jobradar.jaccardsimilarity;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class JaccardSimilarity {

    /**
     * Calculate Jaccard Similarity between two strings.
     *
     * @param stringOne The first string
     * @param stringTwo The second string
     * @return The Jaccard Similarity between the two strings
     */
    public double calculateJaccardSimilarity(String stringOne, String stringTwo) {
        // Create sets to store unique characters from each string
        Set<Character> setOne = new HashSet<>();
        Set<Character> setTwo = new HashSet<>();

        // Populate sets with characters from the first string
        for (char c : stringOne.toCharArray()) {
            setOne.add(c);
        }

        // Populate sets with characters from the second string
        for (char chr : stringTwo.toCharArray()) {
            setTwo.add(chr);
        }

        // Calculate the intersection of the two sets
        Set<Character> intersectionOfBoth = new HashSet<>(setOne);
        intersectionOfBoth.retainAll(setTwo);

        // Calculate the union of the two sets
        Set<Character> unionOfBoth = new HashSet<>(setOne);
        unionOfBoth.addAll(setTwo);

        // Calculate and return the Jaccard Similarity
        return (double) intersectionOfBoth.size() / unionOfBoth.size();
    }
}
