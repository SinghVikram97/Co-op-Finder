package com.acc.jobradar.controller;

import com.acc.jobradar.spellchecker.SpellCheckService;
import com.acc.jobradar.validation.SearchQueryValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class SpellCheckController {
    private final SpellCheckService spellCheckService;
    @GetMapping("/spellcheck/{userInput}")
    public ResponseEntity<String> suggestWords(@PathVariable String userInput) {
        // remove spaces and covert to lowercase
        userInput = userInput.trim().toLowerCase();
        // check if it is valid input
        if(!SearchQueryValidator.validateString(userInput)){
            return ResponseEntity.badRequest().build();
        }
        String correctedString = spellCheckService.spellCheck(userInput);
        return ResponseEntity.ok(correctedString);
    }
}
