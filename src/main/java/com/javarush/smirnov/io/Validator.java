package com.javarush.smirnov.io;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Validator utility for checking file existence and key validity.
 */
public class Validator {

    /**
     * Checks if a file exists and is a regular file.
     *
     * @param filePath    the path to check
     * @return            true if the file exists and is regular, false otherwise
     */
    public boolean isFileExists(String filePath) {
        Path path = Path.of(filePath);
        return Files.isRegularFile(path);
    }

    /**
     * Validates if the key is within the valid range for the alphabet.
     *
     * @param key             the key to validate
     * @param alphabetLength  the length of the alphabet
     * @return                true if key is valid (0 <= key < alphabetLength), false otherwise
     */
    public boolean isValidKey(int key, int alphabetLength) {
        return key >= 0 && key < alphabetLength;
    }
}
