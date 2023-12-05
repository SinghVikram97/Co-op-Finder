package com.acc.jobradar.controller;

import com.acc.jobradar.autocomplete.AutoComplete;
import com.acc.jobradar.validation.SearchQueryValidator;
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
        // remove spaces and covert to lowercase
        userInput = userInput.trim().toLowerCase();
        // check if it is valid input
        if(!SearchQueryValidator.validateString(userInput)){
            return ResponseEntity.badRequest().build();
        }
        List<String> wordSuggestions = autoComplete.suggestWords(userInput);
        return ResponseEntity.ok(wordSuggestions);
    }
}
