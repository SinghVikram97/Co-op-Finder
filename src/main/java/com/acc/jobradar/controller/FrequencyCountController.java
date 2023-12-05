package com.acc.jobradar.controller;

import com.acc.jobradar.frequencycounter.FrequencyCounter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class FrequencyCountController {
    private final FrequencyCounter frequencyCounter;
    @GetMapping("/frequencyCount/{userInput}")
    public ResponseEntity<Map<String, Integer>> frequencyCount(@PathVariable String userInput) {
        Map<String, Integer> wordFrequency = frequencyCounter.getWordFrequency(userInput);
        return ResponseEntity.ok(wordFrequency);
    }
}
