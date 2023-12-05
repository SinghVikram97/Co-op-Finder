package com.acc.jobradar.controller;

import com.acc.jobradar.database.Database;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequiredArgsConstructor
public class VocabController {
    private final Database database;
    @GetMapping("/vocab")
    public ResponseEntity<Set<String>> getVocab(){
        return ResponseEntity.ok(database.getVocabulary());
    }
}
