package com.acc.jobradar.controller;

import com.acc.jobradar.autocomplete.AutoComplete;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AutoCompleteController {
    private final AutoComplete autoComplete;

    @GetMapping("/autocomplete/{userInput}")
    public ResponseEntity<List<String>> getWordSuggestions(@PathVariable String userInput) {
        List<String> wordSuggestions = autoComplete.suggestWords(userInput);
        return ResponseEntity.ok(wordSuggestions);
    }
}
