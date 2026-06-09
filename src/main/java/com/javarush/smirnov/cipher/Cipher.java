package com.javarush.smirnov.cipher;

import com.javarush.smirnov.analyzer.AppConstants;

/**
 * Caesar cipher implementation for encryption and decryption.
 * The cipher shifts characters in the alphabet by a specified key.
 */
public class Cipher {

    /**
     * Encrypts the given text using the Caesar cipher with the specified key.
     * All characters are converted to lowercase before encryption.
     *
     * @param text  the text to encrypt
     * @param key   the shift key (number of positions to shift)
     * @return      the encrypted text
     */
    public String encrypt(String text, int key) {
        text = text.toLowerCase();

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            char symbol = text.charAt(i);
            char shiftedSymbol = shiftCharacter(symbol, key);
            result.append(shiftedSymbol);
        }
        return result.toString();
    }

    /**
     * Decrypts the given text using the Caesar cipher with the specified key.
     * Decryption is performed by shifting characters in the opposite direction.
     *
     * @param text  the text to decrypt
     * @param key   the shift key (number of positions to shift back)
     * @return      the decrypted text
     */
    public String decrypt(String text, int key) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char symbol = text.charAt(i);
            char shiftedSymbol = shiftCharacter(symbol, -key);
            result.append(shiftedSymbol);
        }
        return result.toString();
    }

    /**
     * Shifts a character by the specified key using the alphabet.
     * If the character is not in the alphabet, it is returned unchanged.
     *
     * @param character the character to shift
     * @param key       the shift key (positive for forward, negative for backward)
     * @return          the shifted character or the original if not in alphabet
     */
    private char shiftCharacter(char character, int key) {
        int oldIndex = findIndex(character);
        if (oldIndex == -1) {
            return character;
        }
        // Take old index, add key and wrap around alphabet length
        int newIndex = (oldIndex + key + AppConstants.ALPHABET.length()) % AppConstants.ALPHABET.length();

        return AppConstants.ALPHABET.charAt(newIndex);
    }

    /**
     * Finds the index of a character in the alphabet.
     *
     * @param character the character to find
     * @return          the index of the character in the alphabet, or -1 if not found
     */
    private int findIndex(char character) {
        return AppConstants.ALPHABET.indexOf(character);
    }

    /**
     * Returns the length of the alphabet.
     *
     * @return the alphabet length
     */
    public int getAlphabetLength() {
        return AppConstants.ALPHABET.length();
    }
}
