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
 * Command for decrypting a file.
 * Prompts user for encrypted file, output file, and decryption key.
 * Uses GUI for file selection in GUI mode.
 */
public class DecryptCommand extends Command {

    private MainForm mainForm;

    /**
     * Constructs a DecryptCommand.
     *
     * @param cipher      the cipher instance for decryption
     * @param fileManager the file manager for file operations
     * @param validator   the validator for input validation
     * @param bruteForce  the brute force analyzer (not used in this command)
     * @param statisticalAnalyzer the statistical analyzer (not used in this command)
     */
    public DecryptCommand(Cipher cipher, FileManager fileManager, Validator validator, BruteForce bruteForce, StatisticalAnalyzer statisticalAnalyzer) {
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
        String decryptedFile;
        int key;

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
            decryptedFile = mainForm.getDecryptedFilePath();
            if (decryptedFile.isEmpty()) {
                decryptedFile = "decrypted.txt";
            }
            key = mainForm.getKey();
            
            // Read the encrypted file content
            String content = mainForm.loadFileContent(encryptedFile);
            if (content == null) {
                showError("Не удалось прочитать входной файл");
                return;
            }
            
            // Decrypt the content
            String decryptedContent = cipher.decrypt(content, key);
            
            // Display result in text area
            mainForm.setTextAreaContent(decryptedContent);
            
            // Also save to file for consistency
            fileManager.processDecryptFile(encryptedFile, decryptedFile, key, cipher);
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
            
            System.out.print("Введите путь для сохранения расшифрованного файла: ");
            decryptedFile = scanner.nextLine();
            if (decryptedFile == null || decryptedFile.isEmpty()) {
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
            
            fileManager.processDecryptFile(encryptedFile, decryptedFile, key, cipher);
        }

        if (!validator.isValidKey(key, cipher.getAlphabetLength())) {
            showError("Неверный ключ!");
            return;
        }
        
        showMessage("Файл успешно расшифрован!");
    }

    @Override
    public String getName() {
        return "Расшифровать файл";
    }
}
