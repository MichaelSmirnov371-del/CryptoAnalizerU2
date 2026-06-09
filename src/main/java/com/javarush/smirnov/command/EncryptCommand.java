package com.javarush.smirnov.command;

import com.javarush.smirnov.analyzer.BruteForce;
import com.javarush.smirnov.analyzer.StatisticalAnalyzer;
import com.javarush.smirnov.cipher.Cipher;
import com.javarush.smirnov.io.FileManager;
import com.javarush.smirnov.io.Validator;
import com.javarush.smirnov.gui.MainForm;

import java.io.IOException;
import java.util.Scanner;

/**
 * Command for encrypting a file.
 * Prompts user for input file, output file, and encryption key.
 * Uses GUI for file selection in GUI mode.
 */
public class EncryptCommand extends Command {

    private MainForm mainForm;

    /**
     * Constructs an EncryptCommand.
     *
     * @param cipher      the cipher instance for encryption
     * @param fileManager the file manager for file operations
     * @param validator   the validator for input validation
     * @param bruteForce  the brute force analyzer (not used in this command)
     * @param statisticalAnalyzer the statistical analyzer (not used in this command)
     */
    public EncryptCommand(Cipher cipher, FileManager fileManager, Validator validator, BruteForce bruteForce, StatisticalAnalyzer statisticalAnalyzer) {
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
        String inputFile;
        String outputFile;
        int key;

        if (mainForm != null) {
            // GUI mode: use selected file from openFile button or currentSelectedFile
            inputFile = mainForm.getSelectedFilePath();
            if (inputFile == null || inputFile.isEmpty()) {
                // If no file selected, ask user to select one
                inputFile = mainForm.selectFile();
                if (inputFile == null) {
                    return;
                }
            } else {
                System.out.println("Using selected file: " + inputFile);
            }
            
            // Use a default output file path
            outputFile = mainForm.getEncryptedFilePath();
            if (outputFile.isEmpty()) {
                outputFile = "encrypted.txt";
            }
            
            key = mainForm.getKey();
            
            // Read the input file content
            String content = mainForm.loadFileContent(inputFile);
            if (content == null) {
                showError("Не удалось прочитать входной файл");
                return;
            }
            
            // Encrypt the content
            String encryptedContent = cipher.encrypt(content, key);
            
            // Display result in text area
            mainForm.setTextAreaContent(encryptedContent);
            
            // Also save to file for consistency
            fileManager.processEncryptFile(inputFile, outputFile, key, cipher);
        } else {
            // Console mode: use System.in scanner
            Scanner scanner = new Scanner(System.in);
            
            System.out.print("Введите путь к файлу: ");
            inputFile = scanner.nextLine();
            if (inputFile == null || inputFile.isEmpty()) {
                showError("Файл не указан");
                return;
            }
            
            if (!validator.isFileExists(inputFile)) {
                showError("Файл не найден");
                return;
            }
            
            System.out.print("Введите путь для сохранения результата: ");
            outputFile = scanner.nextLine();
            if (outputFile == null || outputFile.isEmpty()) {
                showError("Путь для сохранения не указан");
                return;
            }
            
            System.out.print("Введите ключ: ");
            try {
                key = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                showError("Ключ должен быть числом");
                return;
            }
            
            if (!validator.isValidKey(key, cipher.getAlphabetLength())) {
                showError("Неверный ключ!");
                return;
            }
            
            fileManager.processEncryptFile(inputFile, outputFile, key, cipher);
        }
        
        showMessage("Файл успешно зашифрован!");
    }

    @Override
    public String getName() {
        return "Зашифровать файл";
    }
}
