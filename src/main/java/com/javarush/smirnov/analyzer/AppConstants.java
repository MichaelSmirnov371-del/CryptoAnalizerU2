package com.javarush.smirnov.analyzer;

/**
 * Analyzer constants used across the analyzer package.
 */
public class AppConstants {

    private AppConstants() {
        // Private constructor to prevent instantiation
    }

    /**
     * Common Russian words used for score calculation in brute force analysis.
     */
    public static final String[] COMMON_WORDS = {
            " что ",
            " это ",
            " как ",
            " она ",
            " они ",
            " для ",
            " был ",
            " была ",
            " его ",
            " все ",
            " уже ",
            " или ",
            " но ",
            " не ",
            " на "
    };

    /**
     * Russian alphabet including punctuation marks and space.
     */
    public static final String ALPHABET = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя.,«»\"':!? ";
}
