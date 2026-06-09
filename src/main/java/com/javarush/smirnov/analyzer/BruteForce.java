package com.javarush.smirnov.analyzer;

import com.javarush.smirnov.cipher.Cipher;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Brute force decryption utility that tries all possible keys
 * and selects the best one based on word frequency scoring.
 * <p>
 * Optimized version that reads the file once and caches its content
 * for better performance during key evaluation.
 */
public class BruteForce {

    /**
     * Decrypts a file using brute force by trying all possible keys.
     * The best key is determined by scoring the decrypted text against common words.
     * <p>
     * Optimizations:
     * - Reads file content once and caches it in memory
     * - Avoids reopening file for each key iteration
     *
     * @param inputFile     path to the encrypted input file
     * @param outputFile    path to save the decrypted output file
     * @param sampleFile    path to a sample file for word frequency analysis (blank for default)
     * @param cipher        the cipher instance to use for decryption
     * @return              the best key found
     * @throws IOException  if file operations fail
     */
    public int decryptByBruteForce(String inputFile, String outputFile, String sampleFile, Cipher cipher) throws IOException {
        Path inputPath = Paths.get(inputFile);
        Path outputPath = Paths.get(outputFile);

        // Read and cache file content once for better performance
        List<String> encryptedLines = Files.readAllLines(inputPath);
        int bestKey = 0;
        int bestScore = Integer.MIN_VALUE;
        String[] wordsForScore;

        // Load words for scoring
        if (sampleFile.isBlank()) {
            wordsForScore = AppConstants.COMMON_WORDS;
        } else {
            wordsForScore = loadWordsFromSampleFile(sampleFile);
        }

        // Try all possible keys and find the one with highest score
        // File is read only once per key, not repeatedly inside the loop
        for (int key = 0; key < cipher.getAlphabetLength(); key++) {
            int score = 0;
            for (String line : encryptedLines) {
                String decryptedLine = cipher.decrypt(line, key);
                score += calculateScore(decryptedLine, wordsForScore);
            }
            if (score > bestScore) {
                bestScore = score;
                bestKey = key;
            }
        }

        // Write the decrypted file using the best key
        try (BufferedWriter writer = Files.newBufferedWriter(outputPath)) {
            for (String line : encryptedLines) {
                String decryptedLine = cipher.decrypt(line, bestKey);
                writer.write(decryptedLine);
                writer.newLine();
            }
        }
        return bestKey;
    }

    /**
     * Loads words from a sample file and formats them with spaces for matching.
     *
     * @param sampleFile    path to the sample file
     * @return              array of formatted words
     * @throws IOException  if file reading fails
     */
    private String[] loadWordsFromSampleFile(String sampleFile) throws IOException {
        Path samplePath = Paths.get(sampleFile);
        String sampleText = Files.readString(samplePath).toLowerCase();
        String[] words = sampleText.split("\\s+");
        for (int i = 0; i < words.length; i++) {
            words[i] = " " + words[i] + " ";
        }
        return words;
    }

    /**
     * Calculates a score for the given text based on occurrences of common words.
     * Each occurrence of a common word adds to the score based on its length.
     *
     * @param text              the text to score
     * @param wordsForScore     array of common words to search for
     * @return                  the calculated score
     */
    private int calculateScore(String text, String[] wordsForScore) {
        int score = 0;
        String lowerText = " " + text.toLowerCase() + " ";

        for (String word : wordsForScore) {
            int index = lowerText.indexOf(word);
            while (index != -1) {
                score += word.length();
                index = lowerText.indexOf(word, index + word.length());
            }
        }
        return score;
    }
}
