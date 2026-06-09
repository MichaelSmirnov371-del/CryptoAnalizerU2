package com.javarush.smirnov.gui;

import com.javarush.smirnov.analyzer.BruteForce;
import com.javarush.smirnov.analyzer.StatisticalAnalyzer;
import com.javarush.smirnov.cipher.Cipher;
import com.javarush.smirnov.io.FileManager;
import com.javarush.smirnov.io.Validator;

/**
 * Main entry point for the smirnov GUI application.
 * Initializes and runs the Swing application.
 */
public class SwingRunner {

    /**
     * Main method that starts the Swing GUI application.
     *
     * @param args  command line arguments (not used)
     */
    public static void main(String[] args) {
        // Initialize components
        Cipher cipher = new Cipher();
        FileManager fileManager = new FileManager();
        Validator validator = new Validator();
        BruteForce bruteForce = new BruteForce();
        StatisticalAnalyzer statisticalAnalyzer = new StatisticalAnalyzer();

        // Create and run GUI
        MainForm mainForm = new MainForm(cipher, fileManager, validator, bruteForce, statisticalAnalyzer);
        mainForm.initialization();
    }
}
