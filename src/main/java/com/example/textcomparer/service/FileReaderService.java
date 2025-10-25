package com.example.textcomparer.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class FileReaderService {

    /**
     * Reads a file and returns a map of words with their frequencies
     * Only words containing alphabetic characters are counted
     * Although we don't use the frequency of each word we count it anyways for flexibility and potential extensibility
     */
    public Map<String, Integer> readFileWords(String filePath) throws IOException {
        Map<String, Integer> wordCount = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Split by non-alphabetic characters
                String[] words = line.toLowerCase().split("[^a-zA-Z]+");

                for (String word : words) {
                    if (!word.isEmpty()) {
                        wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
                    }
                }
            }
        }

        return wordCount;
    }
}
