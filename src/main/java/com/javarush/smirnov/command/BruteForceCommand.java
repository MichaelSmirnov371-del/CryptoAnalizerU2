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
 * Command for brute force decryption.
 * Prompts user for encrypted file and optionally a sample file for scoring.
 * Uses GUI for file selection in GUI mode.
 */
public class BruteForceCommand extends Command {

    private MainForm mainForm;

    /**
     * Constructs a BruteForceCommand.
     *
     * @param cipher              the cipher instance for decryption
     * @param fileManager         the file manager for file operations
     * @param validator           the validator for input validation
     * @param bruteForce          the brute force analyzer
     * @param statisticalAnalyzer the statistical analyzer (not used in this command)
     */
    public BruteForceCommand(Cipher cipher, FileManager fileManager, Validator validator, BruteForce bruteForce, StatisticalAnalyzer statisticalAnalyzer) {
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
        String encryptedFileForBruteForce;
        String bruteForceResultFile;
        String sampleFile;

        if (mainForm != null) {
            // GUI mode: use selected file from openFile button
            encryptedFileForBruteForce = mainForm.getSelectedFilePath();
            if (encryptedFileForBruteForce == null || encryptedFileForBruteForce.isEmpty()) {
                // If no file selected, ask user to select one
                encryptedFileForBruteForce = mainForm.selectFile();
                if (encryptedFileForBruteForce == null) {
                    return;
                }
            }
            bruteForceResultFile = mainForm.getBruteforceFilePath();
            if (bruteForceResultFile.isEmpty()) {
                bruteForceResultFile = "bruteforce.txt";
            }
            
            // Ask if there is a sample file
            Object[] options = {"Да", "Нет"};
            int choice = JOptionPane.showOptionDialog(mainForm,
                    "Есть файл-пример?",
                    "BruteForce",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[1]);
            
            if (choice == 0) {
                sampleFile = mainForm.selectFile();
                if (sampleFile == null) {
                    return;
                }
            } else {
                sampleFile = "";
            }
            
            // Perform brute force decryption
            int foundKey = bruteForce.decryptByBruteForce(
                    encryptedFileForBruteForce,
                    bruteForceResultFile,
                    sampleFile,
                    cipher);
            
            // Read the decrypted content and display in text area
            String decryptedContent = mainForm.loadFileContent(bruteForceResultFile);
            if (decryptedContent != null) {
                mainForm.setTextAreaContent(decryptedContent);
            }
            
            showMessage("Найденный ключ: " + foundKey);
        } else {
            // Console mode: use System.in scanner
            Scanner scanner = new Scanner(System.in);
            
            System.out.print("Введите путь к зашифрованному файлу: ");
            encryptedFileForBruteForce = scanner.nextLine();
            if (encryptedFileForBruteForce == null || encryptedFileForBruteForce.isEmpty()) {
                showError("Файл не указан");
                return;
            }
            
            if (!validator.isFileExists(encryptedFileForBruteForce)) {
                showError("Файл не найден!");
                return;
            }
            
            System.out.print("Введите путь для сохранения результата: ");
            bruteForceResultFile = scanner.nextLine();
            if (bruteForceResultFile == null || bruteForceResultFile.isEmpty()) {
                showError("Путь для сохранения не указан");
                return;
            }
            
            System.out.print("Есть файл-пример? (y/n): ");
            String answer = scanner.nextLine();
            
            if (answer.toLowerCase().equals("y") || answer.toLowerCase().equals("д")) {
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
            } else {
                sampleFile = "";
            }

            int foundKey = bruteForce.decryptByBruteForce(
                    encryptedFileForBruteForce,
                    bruteForceResultFile,
                    sampleFile,
                    cipher);
            showMessage("Найденный ключ: " + foundKey);
        }
    }

    @Override
    public String getName() {
        return "BruteForce";
    }
}
