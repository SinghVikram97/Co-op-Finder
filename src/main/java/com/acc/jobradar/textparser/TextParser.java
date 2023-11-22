package com.acc.jobradar.textparser;

import com.acc.jobradar.filehandler.FileHandler;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TextParser {
    private final FileHandler fileHandler;
    public List<String> extractWords(String input) {
        String[] words = input.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");
        return removeCommonEnglishStopWords(Arrays.stream(words).toList());
    }

    public List<String> removeCommonEnglishStopWords(List<String> wordList){
        List<String> commonEnglishStopWords = fileHandler.getFileContents("common-stop-words.txt");
        return wordList.stream().filter(o -> !commonEnglishStopWords.contains(o)).collect(Collectors.toList());
    }


}
