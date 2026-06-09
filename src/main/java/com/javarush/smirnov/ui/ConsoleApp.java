package com.javarush.smirnov.ui;

import com.javarush.smirnov.analyzer.BruteForce;
import com.javarush.smirnov.analyzer.StatisticalAnalyzer;
import com.javarush.smirnov.command.*;
import com.javarush.smirnov.cipher.Cipher;
import com.javarush.smirnov.io.FileManager;
import com.javarush.smirnov.io.Validator;
import com.javarush.smirnov.gui.MainForm;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Console application with menu for encrypting, decrypting, and analyzing text files.
 * Uses Command pattern for better code organization and maintainability.
 * <p>
 * Also provides optional GUI mode for file selection.
 */
public class ConsoleApp {
    private final Cipher cipher;
    private final FileManager fileManager;
    private final Validator validator;
    private final BruteForce bruteForce;
    private final StatisticalAnalyzer statisticalAnalyzer;
    private final List<Command> commands;
    private final JFrame parentFrame;

    /**
     * Constructs a ConsoleApp and initializes all commands.
     */
    public ConsoleApp() {
        this.cipher = new Cipher();
        this.fileManager = new FileManager();
        this.validator = new Validator();
        this.bruteForce = new BruteForce();
        this.statisticalAnalyzer = new StatisticalAnalyzer();
        this.commands = new ArrayList<>();
        this.parentFrame = new JFrame("CryptoAnalizer U2 - Smirnov");
        parentFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialize all commands with dependencies
        commands.add(new EncryptCommand(cipher, fileManager, validator, bruteForce, statisticalAnalyzer));
        commands.add(new DecryptCommand(cipher, fileManager, validator, bruteForce, statisticalAnalyzer));
        commands.add(new BruteForceCommand(cipher, fileManager, validator, bruteForce, statisticalAnalyzer));
        commands.add(new StatisticalAnalysisCommand(cipher, fileManager, validator, bruteForce, statisticalAnalyzer));
    }

    /**
     * Runs the console application with an interactive menu loop.
     * Users can choose from multiple cipher operations until they exit.
     *
     * @throws IOException  if file operations fail
     */
    public void run() throws IOException {
        while (true) {
            printMenu();
            int choice = readMenuChoice();

            if (choice == 0) {
                System.out.println("Выход");
                return;
            }

            if (choice < 1 || choice > commands.size()) {
                System.out.println("Неверный пункт меню");
                continue;
            }

            Command command = commands.get(choice - 1);
            command.execute();
        }
    }

    /**
     * Runs the GUI application.
     * Use this method to start the graphical interface.
     */
    public void runGui() {
        parentFrame.setVisible(true);
    }

    /**
     * Reads a menu choice integer from user input.
     * Re-prompts until valid integer is entered.
     *
     * @return  the menu choice integer entered by user
     */
    private int readMenuChoice() {
        while (true) {
            String input = new java.util.Scanner(System.in).nextLine();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Пункт меню должен быть числом. Попробуйте еще раз:");
            }
        }
    }

    /**
     * Prints the main menu with available cipher operations.
     */
    private void printMenu() {
        System.out.println("\nДоступные операции:");
        for (int i = 0; i < commands.size(); i++) {
            System.out.println((i + 1) + ". " + commands.get(i).getName());
        }
        System.out.println("0. Выйти");
        System.out.println("Выберите режим: ");
    }

    /**
     * Shows a file selection dialog.
     *
     * @param currentPath the current file path
     * @return the selected file path or null if cancelled
     */
    public String selectFile(String currentPath) {
        JFileChooser fileChooser = new JFileChooser(currentPath);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = fileChooser.showOpenDialog(parentFrame);
        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile().getAbsolutePath();
        }
        return null;
    }
}
