package com.javarush.khmelov.command;

import com.javarush.khmelov.constant.Alphabet;
import com.javarush.khmelov.constant.Const;
import com.javarush.khmelov.entity.Result;
import com.javarush.khmelov.exception.AppException;
import com.javarush.khmelov.util.PathBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class BruteForce extends AbstractAction {

    @Override
    public Result execute(String[] parameters) {
        String encryptedFilename = parameters[0];
        String decryptedFilename = parameters[1];
        int bestKey = 0;
        int maxSpaces = -1;

        for (int key = 0; key < Alphabet.chars.length; key++) {
            int spaceCount = countSpacesInDecryptedFile(encryptedFilename, key);
            if (spaceCount > maxSpaces) {
                maxSpaces = spaceCount;
                bestKey = key;
            }
        }

        return copyWithKey(encryptedFilename, decryptedFilename, bestKey);
    }

    private int countSpacesInDecryptedFile(String filename, int key) {
        char targetChar = ' ';
        int spaceCounter = 0;
        Path filePath = PathBuilder.get(filename);

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            int codePoint;
            while ((codePoint = reader.read()) != -1) {
                char currentChar = (char) codePoint;
                if (Alphabet.index.containsKey(currentChar)) {
                    int originalIndex = Alphabet.index.get(currentChar);
                    int decryptedIndex = (originalIndex + key) % Alphabet.chars.length;
                    if (Alphabet.chars[decryptedIndex] == targetChar) {
                        spaceCounter++;
                    }
                }
            }
        } catch (IOException e) {
            throw new AppException(Const.INCORRECT_FILE + filename, e);
        }

        return spaceCounter;
    }
}