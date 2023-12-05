package com.acc.jobradar.controller;

import com.acc.jobradar.pageranking.PageRanking;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PageRankingController {
    private final PageRanking pageRanking;
    @GetMapping("/pageRanking/{userInput}")
    public ResponseEntity<List<String>> pageRank(@PathVariable String userInput) {
        List<String> pageRanks = pageRanking.getPageRanks(userInput);
        return ResponseEntity.ok(pageRanks);
    }
}
