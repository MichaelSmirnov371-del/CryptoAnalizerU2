package com.javarush.smirnov;

import com.javarush.smirnov.gui.SwingRunner;
import com.javarush.smirnov.ui.ConsoleApp;

import javax.swing.*;
import java.io.IOException;

/**
 * Main entry point for the smirnov application.
 * Shows a dialog to choose between console and GUI modes.
 */
public class MainRunner {

    /**
     * Main method that shows a mode selection dialog.
     *
     * @param args  command line arguments (not used)
     */
    public static void main(String[] args) {
        // Show mode selection dialog
        String[] options = {"Консольный режим", "Графический режим (GUI)"};
        int choice = JOptionPane.showOptionDialog(
                null,
                "Выберите режим работы программы:",
                "CryptoAnalizer U2 - Smirnov",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == 0) {
            // Console mode
            runConsoleApp();
        } else if (choice == 1) {
            // GUI mode
            SwingRunner.main(new String[0]);
        }
        // If user cancelled (choice == -1), do nothing
    }

    private static void runConsoleApp() {
        ConsoleApp app = new ConsoleApp();

        try {
            app.run();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка при работе с файлом:\n" + e.getMessage(),
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
