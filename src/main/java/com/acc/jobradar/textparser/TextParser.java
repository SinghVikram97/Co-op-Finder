package com.acc.jobradar.textparser;

import com.acc.jobradar.constants.RegexConstants;
import com.acc.jobradar.database.Database;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TextParser {
    private final Database database;
    public List<String> extractWords(String input) {
        String[] words = input.replaceAll(RegexConstants.REGEX_NON_ALPHABETS, "").toLowerCase().split(RegexConstants.SPACE_REGEX);
        return removeCommonEnglishStopWords(Arrays.stream(words).toList());
    }

    public List<String> removeCommonEnglishStopWords(List<String> wordList){
        return wordList.stream().filter(o -> !database.getCommonEnglishStopWords().contains(o)).collect(Collectors.toList());
    }


}
