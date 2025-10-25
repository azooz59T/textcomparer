package com.example.textcomparer.model;

public class FileScore implements Comparable<FileScore> {
    private final String fileName;
    private final double score;

    public FileScore(String fileName, double score) {
        this.fileName = fileName;
        this.score = score;
    }

    public String getFileName() {
        return fileName;
    }

    public double getScore() {
        return score;
    }

    @Override
    public int compareTo(FileScore other) {
        // Sort in descending order (highest score first)
        return Double.compare(other.score, this.score);
    }

    @Override
    public String toString() {
        return String.format("%s: %.2f%%", fileName, score);
    }
}