package com.javarush.smirnov.command;

import com.javarush.smirnov.analyzer.BruteForce;
import com.javarush.smirnov.analyzer.StatisticalAnalyzer;
import com.javarush.smirnov.cipher.Cipher;
import com.javarush.smirnov.io.FileManager;
import com.javarush.smirnov.io.Validator;
import com.javarush.smirnov.gui.MainForm;

import javax.swing.*;
import java.io.IOException;

/**
 * Base class for all menu commands in the console application.
 * Implements the Command pattern for better code organization and maintainability.
 */
public abstract class Command {

    protected final Cipher cipher;
    protected final FileManager fileManager;
    protected final Validator validator;
    protected final BruteForce bruteForce;
    protected final StatisticalAnalyzer statisticalAnalyzer;

    /**
     * Constructs a Command with all necessary dependencies.
     *
     * @param cipher              the cipher instance for encryption/decryption
     * @param fileManager         the file manager for file operations
     * @param validator           the validator for input validation
     * @param bruteForce          the brute force analyzer
     * @param statisticalAnalyzer the statistical analyzer
     */
    public Command(Cipher cipher, FileManager fileManager, Validator validator, BruteForce bruteForce, StatisticalAnalyzer statisticalAnalyzer) {
        this.cipher = cipher;
        this.fileManager = fileManager;
        this.validator = validator;
        this.bruteForce = bruteForce;
        this.statisticalAnalyzer = statisticalAnalyzer;
    }

    /**
     * Sets the main form for GUI interaction.
     *
     * @param mainForm the main form
     */
    public void setMainForm(MainForm mainForm) {
        // Default implementation - can be overridden by subclasses
    }

    /**
     * Executes the command.
     *
     * @throws IOException if file operations fail
     */
    public abstract void execute() throws IOException;

    /**
     * Returns the command name for display in the menu.
     *
     * @return the command name
     */
    public abstract String getName();

    /**
     * Shows an error message dialog.
     *
     * @param message the error message to display
     */
    protected void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Ошибка", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Shows a success message dialog.
     *
     * @param message the success message to display
     */
    protected void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Успех", JOptionPane.INFORMATION_MESSAGE);
    }
}
