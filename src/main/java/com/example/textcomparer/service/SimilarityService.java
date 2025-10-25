package com.example.textcomparer.service;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class SimilarityService {

    /**
     * Calculates similarity score between two files based on word overlap
     *
     * Score calculation:
     * - 100% if files contain exactly the same words (regardless of frequency)
     * - 0% if files share no common words
     * - Partial score based on Jaccard similarity (intersection / union)
     */
    public double calculateSimilarityScore(Map<String, Integer> referenceWords,
                                           Map<String, Integer> targetWords) {

        if (referenceWords.isEmpty() && targetWords.isEmpty()) {
            return 100.0;
        }

        if (referenceWords.isEmpty() || targetWords.isEmpty()) {
            return 0.0;
        }

        Set<String> referenceSet = referenceWords.keySet();
        Set<String> targetSet = targetWords.keySet();

        // Calculate intersection
        Set<String> intersection = new HashSet<>(referenceSet);
        intersection.retainAll(targetSet);

        // Calculate union
        Set<String> union = new HashSet<>(referenceSet);
        union.addAll(targetSet);

        // Jaccard similarity: |intersection| / |union|
        return (double) intersection.size() / union.size() * 100.0;
    }
}