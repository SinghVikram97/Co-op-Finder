package com.acc.jobradar.autocomplete;

import java.util.HashMap;

// The class to represent every node in the Trie
public class TrieNode {
    HashMap<Character, TrieNode> children;
    boolean isEndOfWord;

    public TrieNode() {
        children = new HashMap<>();
        isEndOfWord = false;
    }
}
