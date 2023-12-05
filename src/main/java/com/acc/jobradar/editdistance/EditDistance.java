package com.acc.jobradar.editdistance;

import org.springframework.stereotype.Service;

@Service
public class EditDistance {

    // Helper function to calculate the min of three values
    public static int calculateMinimum(int one, int two, int three) {
        return Math.min(Math.min(one, two), three);
    }

    /**
     * Calculate the edit distance of two strings using DP
     *
     *  @param   sourceWord firstWord
     *  @param   targetWord secondword
     *  @return  The editDistance of the two words
     */
    public int calculateEditDistance (String sourceWord, String targetWord) {
        int sourceLength = sourceWord.length();
        int targetLength = targetWord.length();

        // Create a 2D array to store the editDistances
        int[][] editDistanceDPArray = new int[sourceLength + 1][targetLength + 1];

        // Initialize the first row and column of the array
        for (int iChar = 0; iChar <= sourceLength; iChar++) {
            for (int jChar = 0; jChar <= targetLength; jChar++) {
                if (iChar == 0) {
                    // If the source word is empty then the dist is the len of the original word
                    editDistanceDPArray[iChar][jChar] = jChar;
                } else if (jChar == 0) {
                    // If the target word is empty then the dist is the len of the original word
                    editDistanceDPArray[iChar][jChar] = iChar;
                } else if (sourceWord.charAt(iChar - 1) == targetWord.charAt(jChar - 1)) {

                    editDistanceDPArray[iChar][jChar] = editDistanceDPArray[iChar - 1][jChar - 1];
                } else {
                    editDistanceDPArray[iChar][jChar] = 1 + calculateMinimum(
                            editDistanceDPArray[iChar - 1][jChar],
                            editDistanceDPArray[iChar][jChar - 1],
                            editDistanceDPArray[iChar - 1][jChar - 1]
                    );
                }
            }
        }

        // The final element in the array contains the edit distance between the two words
        return editDistanceDPArray[sourceLength][targetLength];
    }
}
