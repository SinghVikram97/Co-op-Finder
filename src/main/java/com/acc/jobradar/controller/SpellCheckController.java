package com.acc.jobradar.controller;

import com.acc.jobradar.datavalidation.SearchQueryValidator;
import com.acc.jobradar.spellchecker.SpellCheckService;
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
        if(SearchQueryValidator.validateString(userInput))
        {
            String correctedString = spellCheckService.spellCheck(userInput);
            return ResponseEntity.ok(correctedString);
        }
        return ResponseEntity.ok(userInput);
    }
}
