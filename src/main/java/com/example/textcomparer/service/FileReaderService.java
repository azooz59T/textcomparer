package com.example.textcomparer.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class FileReaderService {

    // Compile regex pattern once for performance
    private static final Pattern WORD_PATTERN = Pattern.compile("[^a-zA-Z]+");

    /**
     * Initial capacity for HashMap to avoid resizing.
     * Calculated for ~200K unique words (worst case for 10M word file).
     * Formula: 200,000 / 0.75 = 266,666 → next power of 2 = 262,144
     */
    private static final int INITIAL_CAPACITY = 262_144;

    /**
     * Reads a file and returns a map of words to their frequencies.
     * Optimized with precompiled regex and merge operation.
     */
    public Map<String, Integer> readFileWords(String filePath) throws IOException {
        // Reasonable initial capacity for typical documents
        Map<String, Integer> wordCount = new HashMap<>(INITIAL_CAPACITY);

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Use precompiled pattern for better performance
                String[] words = WORD_PATTERN.split(line.toLowerCase());

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