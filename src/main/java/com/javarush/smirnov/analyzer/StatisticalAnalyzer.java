package com.javarush.smirnov.analyzer;

import com.javarush.smirnov.cipher.Cipher;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Statistical analyzer that uses frequency analysis to decrypt text.
 * Compares character frequencies in encrypted text with sample text to find the key.
 */
public class StatisticalAnalyzer {

    /**
     * Decrypts a file using statistical frequency analysis.
     * Compares character frequency distributions to find the optimal key.
     *
     * @param inputFile     path to the encrypted input file
     * @param outputFile    path to save the decrypted output file
     * @param sampleFile    path to the sample file for frequency comparison
     * @param cipher        the cipher instance to use for decryption
     * @return              the best key found
     * @throws IOException  if file operations fail
     */
    public int decryptByStatistics(
            String inputFile,
            String outputFile,
            String sampleFile,
            Cipher cipher
    ) throws IOException {
        double[] sampleFrequencies = calculateFrequencies(sampleFile);
        double[] encryptedFrequencies = calculateFrequencies(inputFile);
        int bestKey = 0;
        double bestDeviation = Double.MAX_VALUE;

        // Find the key with minimum deviation between frequency distributions
        for (int key = 0; key < cipher.getAlphabetLength(); key++) {
            double deviation = calculateDeviation(encryptedFrequencies, sampleFrequencies, key);
            if (deviation < bestDeviation) {
                bestDeviation = deviation;
                bestKey = key;
            }
        }
        decryptFile(inputFile, outputFile, bestKey, cipher);
        return bestKey;
    }

    /**
     * Decrypts a file using the specified key and cipher.
     *
     * @param inputFile     path to the encrypted input file
     * @param outputFile    path to save the decrypted output file
     * @param key           the decryption key
     * @param cipher        the cipher instance to use for decryption
     * @throws IOException  if file operations fail
     */
    private void decryptFile(String inputFile, String outputFile, int key, Cipher cipher) throws IOException {
        Path inputPath = Path.of(inputFile);
        Path outputPath = Path.of(outputFile);

        try (BufferedReader reader = Files.newBufferedReader(inputPath);
             BufferedWriter writer = Files.newBufferedWriter(outputPath)
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                String decryptedLine = cipher.decrypt(line, key);
                writer.write(decryptedLine);
                writer.newLine();
            }
        }
    }

    /**
     * Calculates character frequency distribution for the given file.
     * Returns an array of frequencies for each character in the alphabet.
     *
     * @param filePath    path to the file to analyze
     * @return            array of character frequencies
     * @throws IOException  if file reading fails
     */
    private double[] calculateFrequencies(String filePath) throws IOException {
        double[] frequencies = new double[AppConstants.ALPHABET.length()];
        int totalCharacters = 0;

        Path path = Path.of(filePath);

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.toLowerCase();

                for (int i = 0; i < line.length(); i++) {
                    char character = line.charAt(i);
                    int index = AppConstants.ALPHABET.indexOf(character);
                    if (index != -1) {
                        frequencies[index]++;
                        totalCharacters++;
                    }
                }
            }
        }
        if (totalCharacters > 0) {
            for (int i = 0; i < frequencies.length; i++) {
                frequencies[i] = frequencies[i] / totalCharacters;
            }
        }
        return frequencies;
    }

    /**
     * Calculates the deviation between encrypted text frequencies and sample frequencies
     * for a given key. Used to find the optimal decryption key.
     *
     * @param encryptedFrequencies    character frequencies of encrypted text
     * @param sampleFrequencies       character frequencies of sample text
     * @param key                     the key to test
     * @return                        the calculated deviation (lower is better)
     */
    private double calculateDeviation(double[] encryptedFrequencies, double[] sampleFrequencies, int key) {
        double deviation = 0;
        int alphabetLength = AppConstants.ALPHABET.length();

        for (int i = 0; i < alphabetLength; i++) {
            int shiftedIndex = (i + key) % alphabetLength;
            double difference = sampleFrequencies[i] - encryptedFrequencies[shiftedIndex];
            deviation += difference * difference;
        }
        return deviation;
    }
}
