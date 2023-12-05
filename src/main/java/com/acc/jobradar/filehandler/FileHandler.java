package com.acc.jobradar.filehandler;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    public static List<String> getFileContents(String fileName) {
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

    public static void saveHtmlToFile(String htmlSource, String folderPath, String fileName) {
        try {
            // Create a BufferedWriter to write to the file
            BufferedWriter writer = new BufferedWriter(new FileWriter(folderPath + "/" + fileName + ".html"));

            // Write the HTML source to the file
            writer.write(htmlSource);

            // Close the writer
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getFileNameFromUrl(String url) {
        // Extract the filename from the URL
        return Integer.toHexString(url.hashCode());
    }
}
