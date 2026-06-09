package com.javarush.smirnov.command;

import com.javarush.smirnov.analyzer.BruteForce;
import com.javarush.smirnov.analyzer.StatisticalAnalyzer;
import com.javarush.smirnov.cipher.Cipher;
import com.javarush.smirnov.io.FileManager;
import com.javarush.smirnov.io.Validator;
import com.javarush.smirnov.gui.MainForm;

import javax.swing.*;
import java.io.IOException;
import java.util.Scanner;

/**
 * Command for statistical analysis decryption.
 * Prompts user for input files and performs frequency-based decryption.
 * Uses GUI for file selection in GUI mode.
 */
public class StatisticalAnalysisCommand extends Command {

    private MainForm mainForm;

    /**
     * Constructs a StatisticalAnalysisCommand.
     *
     * @param cipher              the cipher instance for decryption
     * @param fileManager         the file manager for file operations
     * @param validator           the validator for input validation
     * @param bruteForce          the brute force analyzer (not used in this command)
     * @param statisticalAnalyzer the statistical analyzer
     */
    public StatisticalAnalysisCommand(Cipher cipher, FileManager fileManager, Validator validator, BruteForce bruteForce, StatisticalAnalyzer statisticalAnalyzer) {
        super(cipher, fileManager, validator, bruteForce, statisticalAnalyzer);
    }

    /**
     * Sets the main form for GUI interaction.
     *
     * @param mainForm the main form
     */
    public void setMainForm(MainForm mainForm) {
        this.mainForm = mainForm;
    }

    @Override
    public void execute() throws IOException {
        String encryptedFile;
        String outputFile;
        String sampleFile;

        if (mainForm != null) {
            // GUI mode: use selected file from openFile button
            encryptedFile = mainForm.getSelectedFilePath();
            if (encryptedFile == null || encryptedFile.isEmpty()) {
                // If no file selected, ask user to select one
                encryptedFile = mainForm.selectFile();
                if (encryptedFile == null) {
                    return;
                }
            }
            outputFile = mainForm.getAnalyzedFilePath();
            if (outputFile.isEmpty()) {
                outputFile = "analyzed.txt";
            }
            sampleFile = mainForm.selectFile();
            if (sampleFile == null) {
                return;
            }
            
            // Perform statistical analysis decryption
            int foundKey = statisticalAnalyzer.decryptByStatistics(
                    encryptedFile,
                    outputFile,
                    sampleFile,
                    cipher
            );
            
            // Read the decrypted content and display in text area
            String decryptedContent = mainForm.loadFileContent(outputFile);
            if (decryptedContent != null) {
                mainForm.setTextAreaContent(decryptedContent);
            }
            
            showMessage("Найденный ключ: " + foundKey);
        } else {
            // Console mode: use System.in scanner
            Scanner scanner = new Scanner(System.in);
            
            System.out.print("Введите путь к зашифрованному файлу: ");
            encryptedFile = scanner.nextLine();
            if (encryptedFile == null || encryptedFile.isEmpty()) {
                showError("Файл не указан");
                return;
            }
            
            if (!validator.isFileExists(encryptedFile)) {
                showError("Файл не найден!");
                return;
            }
            
            System.out.print("Введите путь для сохранения результата: ");
            outputFile = scanner.nextLine();
            if (outputFile == null || outputFile.isEmpty()) {
                showError("Путь для сохранения не указан");
                return;
            }
            
            System.out.print("Введите путь к файлу-примеру: ");
            sampleFile = scanner.nextLine();
            if (sampleFile == null || sampleFile.isEmpty()) {
                showError("Файл-пример не указан");
                return;
            }
            
            if (!validator.isFileExists(sampleFile)) {
                showError("Файл-пример не найден!");
                return;
            }
        }

        int foundKey = statisticalAnalyzer.decryptByStatistics(
                encryptedFile,
                outputFile,
                sampleFile,
                cipher
        );

        showMessage("Найденный ключ: " + foundKey);
    }

    @Override
    public String getName() {
        return "Статистический анализ";
    }
}
