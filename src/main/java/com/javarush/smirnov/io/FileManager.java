package com.javarush.smirnov.io;

import com.javarush.smirnov.cipher.Cipher;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * File manager utility for reading and writing encrypted/decrypted files.
 */
public class FileManager {

    /**
     * Encrypts a file and writes the result to the output file.
     *
     * @param inputFile     path to the input file to encrypt
     * @param outputFile    path to save the encrypted output file
     * @param key           the encryption key
     * @param cipher        the cipher instance to use for encryption
     * @throws IOException  if file operations fail
     */
    public void processEncryptFile(String inputFile, String outputFile, int key, Cipher cipher) throws IOException {
        Path inputPath = Path.of(inputFile);
        Path outputPath = Path.of(outputFile);

        try (BufferedReader reader = Files.newBufferedReader(inputPath);
             BufferedWriter writer = Files.newBufferedWriter(outputPath)) {
            String line;

            while ((line = reader.readLine()) != null) {
                String encryptedLine = cipher.encrypt(line, key);
                writer.write(encryptedLine);
                writer.newLine();
            }
        }
    }

    /**
     * Decrypts a file and writes the result to the output file.
     *
     * @param inputFile     path to the encrypted input file
     * @param outputFile    path to save the decrypted output file
     * @param key           the decryption key
     * @param cipher        the cipher instance to use for decryption
     * @throws IOException  if file operations fail
     */
    public void processDecryptFile(String inputFile, String outputFile, int key, Cipher cipher) throws IOException {
        Path inputPath = Path.of(inputFile);
        Path outputPath = Path.of(outputFile);

        try (BufferedReader reader = Files.newBufferedReader(inputPath);
             BufferedWriter writer = Files.newBufferedWriter(outputPath)) {
            String line;

            while ((line = reader.readLine()) != null) {
                String decryptedLine = cipher.decrypt(line, key);
                writer.write(decryptedLine);
                writer.newLine();
            }
        }
    }

    /**
     * Saves text content to a file.
     *
     * @param content       the text content to save
     * @param outputFile    path to save the output file
     * @throws IOException  if file operations fail
     */
    public void saveTextToFile(String content, String outputFile) throws IOException {
        Path outputPath = Path.of(outputFile);

        try (BufferedWriter writer = Files.newBufferedWriter(outputPath)) {
            writer.write(content);
        }
    }
}
