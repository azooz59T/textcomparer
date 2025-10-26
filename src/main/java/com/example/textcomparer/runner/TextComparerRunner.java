package com.example.textcomparer.runner;

import com.example.textcomparer.model.FileScore;
import com.example.textcomparer.service.FileReaderService;
import com.example.textcomparer.service.SimilarityService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class TextComparerRunner implements CommandLineRunner {

    private final FileReaderService fileReaderService;
    private final SimilarityService similarityService;

    @Value("${file.reference.path}")
    private String referencePath;

    @Value("${file.pool.directory}")
    private String poolDirectory;

    public TextComparerRunner(FileReaderService fileReaderService,
                              SimilarityService similarityService) {
        this.fileReaderService = fileReaderService;
        this.similarityService = similarityService;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("=== Text Comparer Starting ===\n");

        // Read reference file
        System.out.println("Reading reference file: " + referencePath);
        Map<String, Integer> referenceWords = fileReaderService.readFileWords(referencePath);
        System.out.println("Reference file contains " + referenceWords.size() + " unique words\n");

        // Read pool directory
        File poolDir = new File(poolDirectory);
        if (!poolDir.exists() || !poolDir.isDirectory()) {
            System.err.println("Error: Pool directory does not exist: " + poolDirectory);
            return;
        }

        File[] poolFiles = poolDir.listFiles((dir, name) -> name.endsWith(".txt"));
        if (poolFiles == null || poolFiles.length == 0) {
            System.err.println("Error: No .txt files found in pool directory");
            return;
        }

        System.out.println("Found " + poolFiles.length + " files in pool directory\n");

        // Calculate scores for each file
        List<FileScore> scores = new ArrayList<>();

        for (File file : poolFiles) {
            try {
                Map<String, Integer> fileWords = fileReaderService.readFileWords(file.getAbsolutePath());
                double score = similarityService.calculateSimilarityScore(referenceWords, fileWords);
                scores.add(new FileScore(file.getName(), score));

                System.out.println("Processed: " + file.getName() +
                        " (" + fileWords.size() + " unique words)");
            } catch (IOException e) {
                System.err.println("Error reading file " + file.getName() + ": " + e.getMessage());
            }
        }

        // Sort scores (highest first)
        Collections.sort(scores);

        // Display results
        System.out.println("\n=== Results (sorted by similarity) ===");
        for (FileScore score : scores) {
            System.out.println(score);
        }

        System.out.println("\nBest match: " + scores.get(0).getFileName() +
                " with score " + String.format("%.2f%%", scores.get(0).getScore()));
    }
}