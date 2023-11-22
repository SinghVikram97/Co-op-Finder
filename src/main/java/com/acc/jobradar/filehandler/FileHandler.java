package com.acc.jobradar.filehandler;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileHandler {
    public List<String> getFileContents(String fileName) {
        List<String> fileContent = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                fileContent.add(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fileContent;
    }
}
