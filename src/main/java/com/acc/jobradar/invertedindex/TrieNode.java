package com.acc.jobradar.invertedindex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Each node represents a character in a word
public class TrieNode {
    // children map to store references to child nodes
    // keys are characters, and the values are the corresponding child nodes
    Map<Character, TrieNode> children = new HashMap<>();
    // The jobIds list is used to store the job IDs associated with the word formed by the characters from the root of the Trie to the current node
    List<String> jobIds = new ArrayList<>();
}
