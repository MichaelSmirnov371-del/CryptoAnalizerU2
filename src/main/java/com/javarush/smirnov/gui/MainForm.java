package com.javarush.smirnov.gui;

import com.javarush.smirnov.analyzer.BruteForce;
import com.javarush.smirnov.analyzer.StatisticalAnalyzer;
import com.javarush.smirnov.command.*;
import com.javarush.smirnov.cipher.Cipher;
import com.javarush.smirnov.io.FileManager;
import com.javarush.smirnov.io.Validator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Main GUI window for the smirnov application.
 * Provides graphical interface for encrypting, decrypting, and analyzing text files.
 */
public class MainForm extends JFrame {

    private static final int PERCENT_SCREEN = 70;

    private final Cipher cipher;
    private final FileManager fileManager;
    private final Validator validator;
    private final BruteForce bruteForce;
    private final StatisticalAnalyzer statisticalAnalyzer;
    private final Command[] commands;

    private JTextArea textArea;
    private JTextField textFilePath;
    private JTextField encryptedFilePath;
    private JTextField decryptedFilePath;
    private JTextField bruteforceFilePath;
    private JTextField analyzedFilePath;
    private JTextField dictFilePath;
    private JTextField keyField;
    private JLabel currentFilename;
    private JButton setTextButton;
    private JButton setEncryptedButton;
    private JButton setDecryptedButton;
    private JButton setBruteforceButton;
    private JButton setDictButton;
    private JButton setAnalyzedButton;
    private JComboBox<String> commandComboBox;
    private String currentSelectedFile = "";

    /**
     * Constructs a MainForm with all dependencies.
     *
     * @param cipher              the cipher instance
     * @param fileManager         the file manager
     * @param validator           the validator
     * @param bruteForce          the brute force analyzer
     * @param statisticalAnalyzer the statistical analyzer
     */
    public MainForm(Cipher cipher, FileManager fileManager, Validator validator,
                   BruteForce bruteForce, StatisticalAnalyzer statisticalAnalyzer) {
        this.cipher = cipher;
        this.fileManager = fileManager;
        this.validator = validator;
        this.bruteForce = bruteForce;
        this.statisticalAnalyzer = statisticalAnalyzer;
        this.commands = new Command[4];

        // Initialize commands
        commands[0] = new EncryptCommand(cipher, fileManager, validator, bruteForce, statisticalAnalyzer);
        commands[1] = new DecryptCommand(cipher, fileManager, validator, bruteForce, statisticalAnalyzer);
        commands[2] = new BruteForceCommand(cipher, fileManager, validator, bruteForce, statisticalAnalyzer);
        commands[3] = new StatisticalAnalysisCommand(cipher, fileManager, validator, bruteForce, statisticalAnalyzer);

        // Set main form for GUI interaction
        for (Command command : commands) {
            if (command instanceof EncryptCommand) {
                ((EncryptCommand) command).setMainForm(this);
            } else if (command instanceof DecryptCommand) {
                ((DecryptCommand) command).setMainForm(this);
            } else if (command instanceof BruteForceCommand) {
                ((BruteForceCommand) command).setMainForm(this);
            } else if (command instanceof StatisticalAnalysisCommand) {
                ((StatisticalAnalysisCommand) command).setMainForm(this);
            }
        }

        setTitle("CryptoAnalizer U2 - Smirnov");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIcon();
        setDefaultLookAndFeel();
    }

    /**
     * Initializes and displays the GUI.
     */
    public void initialization() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());
        
        initView();
        setDefaultValues();
        
        setLocationRelativeTo(null);
        setVisible(true);
        validate();
    }

    private void setIcon() {
        URL png = getClass().getResource("/icon.png");
        if (png != null) {
            Image image = Toolkit.getDefaultToolkit().getImage(png);
            setIconImage(image);
        }
    }

    private static void setDefaultLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Failed to set system look and feel: " + e.getMessage());
        }
    }

    private void initView() {
        // Create main panel with grid layout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Text area panel
        JPanel textPanel = new JPanel(new BorderLayout());
        textArea = new JTextArea(10, 40);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(textArea);
        textPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(textPanel, BorderLayout.CENTER);

        // Input/Output panel
        JPanel ioPanel = createIOPanel();
        mainPanel.add(ioPanel, BorderLayout.NORTH);
        System.out.println("IO panel added to main panel");

        // Buttons panel
        JPanel buttonsPanel = createButtonsPanel();
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);
        System.out.println("Buttons panel added to main panel");

        // Status bar
        JPanel statusPanel = new JPanel(new BorderLayout());
        currentFilename = new JLabel("No file loaded");
        statusPanel.add(currentFilename, BorderLayout.WEST);
        mainPanel.add(statusPanel, BorderLayout.NORTH);
        System.out.println("Status panel added to main panel");

        // Add action listeners for file selection buttons
        setTextButton.addActionListener(e -> {
            String selectedFile = selectFile();
            if (selectedFile != null) {
                textFilePath.setText(selectedFile);
                loadFile(selectedFile);
            }
        });

        setEncryptedButton.addActionListener(e -> {
            String selectedFile = selectFile();
            if (selectedFile != null) {
                encryptedFilePath.setText(selectedFile);
            }
        });

        setDecryptedButton.addActionListener(e -> {
            String selectedFile = selectFile();
            if (selectedFile != null) {
                decryptedFilePath.setText(selectedFile);
            }
        });

        setBruteforceButton.addActionListener(e -> {
            String selectedFile = selectFile();
            if (selectedFile != null) {
                bruteforceFilePath.setText(selectedFile);
            }
        });

        setDictButton.addActionListener(e -> {
            String selectedFile = selectFile();
            if (selectedFile != null) {
                dictFilePath.setText(selectedFile);
            }
        });

        setAnalyzedButton.addActionListener(e -> {
            String selectedFile = selectFile();
            if (selectedFile != null) {
                analyzedFilePath.setText(selectedFile);
            }
        });

        add(mainPanel);
        
        // Debug output
        System.out.println("Main panel added to frame");
        System.out.println("Main panel components: " + mainPanel.getComponentCount());
        System.out.println("Buttons panel: " + buttonsPanel);
        System.out.println("Buttons panel visible: " + buttonsPanel.isVisible());
    }

    private JPanel createIOPanel() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));

        // Text file input
        panel.add(new JLabel("Исходный файл:"));
        JPanel textPanel = new JPanel(new BorderLayout());
        textFilePath = new JTextField(20);
        textPanel.add(textFilePath, BorderLayout.CENTER);
        setTextButton = new JButton("...");
        textPanel.add(setTextButton, BorderLayout.EAST);
        panel.add(textPanel);

        // Encrypted file input
        panel.add(new JLabel("Зашифрованный файл:"));
        JPanel encryptedPanel = new JPanel(new BorderLayout());
        encryptedFilePath = new JTextField(20);
        encryptedPanel.add(encryptedFilePath, BorderLayout.CENTER);
        setEncryptedButton = new JButton("...");
        encryptedPanel.add(setEncryptedButton, BorderLayout.EAST);
        panel.add(encryptedPanel);

        // Decrypted file input
        panel.add(new JLabel("Расшифрованный файл:"));
        JPanel decryptedPanel = new JPanel(new BorderLayout());
        decryptedFilePath = new JTextField(20);
        decryptedPanel.add(decryptedFilePath, BorderLayout.CENTER);
        setDecryptedButton = new JButton("...");
        decryptedPanel.add(setDecryptedButton, BorderLayout.EAST);
        panel.add(decryptedPanel);

        // Bruteforce file input
        panel.add(new JLabel("Bruteforce файл:"));
        JPanel bruteforcePanel = new JPanel(new BorderLayout());
        bruteforceFilePath = new JTextField(20);
        bruteforcePanel.add(bruteforceFilePath, BorderLayout.CENTER);
        setBruteforceButton = new JButton("...");
        bruteforcePanel.add(setBruteforceButton, BorderLayout.EAST);
        panel.add(bruteforcePanel);

        // Dict file input
        panel.add(new JLabel("Файл словаря:"));
        JPanel dictPanel = new JPanel(new BorderLayout());
        dictFilePath = new JTextField(20);
        dictPanel.add(dictFilePath, BorderLayout.CENTER);
        setDictButton = new JButton("...");
        dictPanel.add(setDictButton, BorderLayout.EAST);
        panel.add(dictPanel);

        // Analyzed file input
        panel.add(new JLabel("Анализируемый файл:"));
        JPanel analyzedPanel = new JPanel(new BorderLayout());
        analyzedFilePath = new JTextField(20);
        analyzedPanel.add(analyzedFilePath, BorderLayout.CENTER);
        setAnalyzedButton = new JButton("...");
        analyzedPanel.add(setAnalyzedButton, BorderLayout.EAST);
        panel.add(analyzedPanel);

        return panel;
    }

    private JPanel createButtonsPanel() {
        System.out.println("Creating buttons panel...");
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top panel for command selection
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        topPanel.setBorder(BorderFactory.createTitledBorder("Выберите операцию:"));
        
        String[] commandNames = {"Зашифровать", "Расшифровать", "BruteForce", "Статистический анализ"};
        commandComboBox = new JComboBox<>(commandNames);
        topPanel.add(commandComboBox);
        panel.add(topPanel);
        System.out.println("Added command combo box");

        // Key input
        JPanel keyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        keyPanel.add(new JLabel("Ключ:"));
        keyField = new JTextField("1", 5);
        keyPanel.add(keyField);
        panel.add(keyPanel);
        System.out.println("Added key field");

        // Buttons row
        JPanel buttonsRowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        
        // Execute and save button
        JButton executeAndSaveButton = new JButton(new AbstractAction("Выполнить и сохранить результат") {
            @Override
            public void actionPerformed(ActionEvent e) {
                executeAndSave();
            }
        });
        executeAndSaveButton.setFont(new Font("SansSerif", Font.BOLD, 12));
        buttonsRowPanel.add(executeAndSaveButton);
        System.out.println("Added execute and save button");
        
        // Open file button
        JButton openButton = new JButton(new AbstractAction("Открыть файл") {
            @Override
            public void actionPerformed(ActionEvent e) {
                openFile();
            }
        });
        buttonsRowPanel.add(openButton);
        System.out.println("Added open button");
        
        panel.add(buttonsRowPanel);
        System.out.println("Added buttons row panel, total components: " + panel.getComponentCount());
        
        // Set preferred size for better visibility
        panel.setPreferredSize(new Dimension(800, 200));

        return panel;
    }

    private void setDefaultValues() {
        // Set default file paths
        textFilePath.setText("text.txt");
        encryptedFilePath.setText("encrypted.txt");
        decryptedFilePath.setText("decrypted.txt");
        bruteforceFilePath.setText("bruteforce.txt");
        analyzedFilePath.setText("analyzed.txt");
        dictFilePath.setText("dict.txt");
    }

    /**
     * Saves current text area content to the appropriate output file based on selected command.
     */
    private void saveResult() {
        int selectedIndex = commandComboBox.getSelectedIndex();
        String filePath = "";
        String message = "";

        switch (selectedIndex) {
            case 0: // Encrypt
                filePath = getEncryptedFilePath();
                message = "Файл зашифрован и сохранён";
                break;
            case 1: // Decrypt
                filePath = getDecryptedFilePath();
                message = "Файл расшифрован и сохранён";
                break;
            case 2: // BruteForce
                filePath = getBruteforceFilePath();
                message = "Результат BruteForce сохранён";
                break;
            case 3: // Statistical Analysis
                filePath = getAnalyzedFilePath();
                message = "Результат анализа сохранён";
                break;
        }

        if (filePath.isEmpty()) {
            showError("Не указан путь к файлу!");
            return;
        }

        if (saveFile(filePath)) {
            showMessage(message);
        }
    }

    private void openFile() {
        System.out.println("Open file button clicked");
        String filePath = selectFile();
        if (filePath != null) {
            System.out.println("Loading file: " + filePath);
            loadFile(filePath);
            currentSelectedFile = filePath;
            System.out.println("Current selected file saved: " + currentSelectedFile);
        }
    }

    /**
     * Executes the selected command and saves the result.
     */
    private void executeAndSave() {
        System.out.println("Execute and save button clicked");
        int selectedIndex = commandComboBox.getSelectedIndex();
        
        // If file is selected, load it first
        if (currentSelectedFile != null && !currentSelectedFile.isEmpty()) {
            System.out.println("Loading selected file: " + currentSelectedFile);
            loadFile(currentSelectedFile);
        }
        
        // Execute the command first
        Command command = commands[selectedIndex];
        try {
            System.out.println("Executing command...");
            command.execute();
            System.out.println("Command executed successfully");
        } catch (IOException e) {
            System.out.println("Error executing command: " + e.getMessage());
            showError("Ошибка при работе с файлом:\n" + e.getMessage());
            return;
        }
        
        // Show save dialog to select output file for the result
        // Use default filename based on command type
        String defaultFileName = "result.txt";
        switch (selectedIndex) {
            case 0: // Encrypt
                defaultFileName = "encrypted_result.txt";
                break;
            case 1: // Decrypt
                defaultFileName = "decrypted_result.txt";
                break;
            case 2: // BruteForce
                defaultFileName = "bruteforce_result.txt";
                break;
            case 3: // Statistical Analysis
                defaultFileName = "analysis_result.txt";
                break;
        }
        
        String outputFilePath = selectSaveFile(defaultFileName);
        if (outputFilePath == null) {
            System.out.println("Save cancelled");
            return;
        }
        
        // Ensure .txt extension
        if (!outputFilePath.toLowerCase().endsWith(".txt")) {
            outputFilePath += ".txt";
        }
        
        System.out.println("Saving result to: " + outputFilePath);
        
        // Save the result from text area to the selected location
        if (saveFile(outputFilePath)) {
            showMessage("Файл успешно сохранён!");
        }
    }

    private void runCommand(int commandIndex) {
        System.out.println("Running command: " + commandIndex);
        System.out.println("Current selected file: " + currentSelectedFile);
        
        Command command = commands[commandIndex];
        try {
            // If file is selected, load it first
            if (currentSelectedFile != null && !currentSelectedFile.isEmpty()) {
                System.out.println("Loading selected file: " + currentSelectedFile);
                loadFile(currentSelectedFile);
            }
            
            // Execute command with UI interaction
            command.execute();
            System.out.println("Command executed successfully");
        } catch (IOException e) {
            System.out.println("Error executing command: " + e.getMessage());
            showError("Ошибка при работе с файлом:\n" + e.getMessage());
        }
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Ошибка", JOptionPane.ERROR_MESSAGE);
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Успех", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Loads file content into the text area.
     *
     * @param filePath the path to the file
     */
    public void loadFile(String filePath) {
        try {
            File file = new File(filePath);
            if (file.exists() && file.isFile()) {
                StringBuilder content = new StringBuilder();
                try (java.io.BufferedReader reader = new java.io.BufferedReader(
                        new java.io.FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        content.append(line).append("\n");
                    }
                }
                textArea.setText(content.toString().trim());
                currentFilename.setText("Файл: " + filePath);
            } else {
                showError("Файл не найден: " + filePath);
            }
        } catch (IOException e) {
            showError("Ошибка при чтении файла:\n" + e.getMessage());
        }
    }

    /**
     * Loads file content without updating UI.
     *
     * @param filePath the path to the file
     * @return the file content or null if error
     */
    public String loadFileContent(String filePath) {
        try {
            File file = new File(filePath);
            if (file.exists() && file.isFile()) {
                StringBuilder content = new StringBuilder();
                try (java.io.BufferedReader reader = new java.io.BufferedReader(
                        new java.io.FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        content.append(line).append("\n");
                    }
                }
                return content.toString().trim();
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return null;
    }

    /**
     * Sets text area content without loading from file.
     *
     * @param content the content to set
     */
    public void setTextAreaContent(String content) {
        textArea.setText(content);
    }

    /**
     * Saves text area content to a file.
     *
     * @param filePath the path to save the file
     * @return true if successful, false otherwise
     */
    public boolean saveFile(String filePath) {
        try {
            File file = new File(filePath);
            try (java.io.BufferedWriter writer = new java.io.BufferedWriter(
                    new java.io.FileWriter(file))) {
                writer.write(textArea.getText());
            }
            currentFilename.setText("Сохранено: " + filePath);
            return true;
        } catch (IOException e) {
            showError("Ошибка при сохранении файла:\n" + e.getMessage());
            return false;
        }
    }

    /**
     * Shows a file dialog for saving and returns the selected file path.
     * Default path is the Desktop folder.
     *
     * @param defaultFileName the default file name to suggest
     * @return the selected file path or null if cancelled
     */
    public String selectSaveFile(String defaultFileName) {
        System.out.println("Opening save file dialog...");
        // Get Desktop folder path
        String desktopPath = System.getProperty("user.home");
        System.out.println("Desktop path: " + desktopPath);
        
        JFileChooser fileChooser = new JFileChooser(desktopPath);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setSelectedFile(new java.io.File(defaultFileName));
        int result = fileChooser.showSaveDialog(this);
        System.out.println("Save result: " + result);
        if (result == JFileChooser.APPROVE_OPTION) {
            String selectedPath = fileChooser.getSelectedFile().getAbsolutePath();
            System.out.println("Save selected file: " + selectedPath);
            return selectedPath;
        }
        System.out.println("Save cancelled or error");
        return null;
    }

    /**
     * Shows a file dialog and returns the selected file path.
     * Default path is the Desktop folder.
     *
     * @return the selected file path or null if cancelled
     */
    public String selectFile() {
        System.out.println("Opening file dialog...");
        // Get Desktop folder path
        String desktopPath = System.getProperty("user.home");
        System.out.println("Desktop path: " + desktopPath);
        
        JFileChooser fileChooser = new JFileChooser(desktopPath);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = fileChooser.showOpenDialog(this);
        System.out.println("Result: " + result);
        if (result == JFileChooser.APPROVE_OPTION) {
            String selectedPath = fileChooser.getSelectedFile().getAbsolutePath();
            System.out.println("Selected file: " + selectedPath);
            return selectedPath;
        }
        System.out.println("File selection cancelled or error");
        return null;
    }

    /**
     * Gets the input file path from text field.
     *
     * @return the file path
     */
    public String getTextFilePath() {
        return textFilePath.getText().trim();
    }

    /**
     * Gets the encrypted file path from text field.
     *
     * @return the file path
     */
    public String getEncryptedFilePath() {
        return encryptedFilePath.getText().trim();
    }

    /**
     * Gets the decrypted file path from text field.
     *
     * @return the file path
     */
    public String getDecryptedFilePath() {
        return decryptedFilePath.getText().trim();
    }

    /**
     * Gets the key from key field.
     *
     * @return the key value
     */
    public int getKey() {
        try {
            return Integer.parseInt(keyField.getText().trim());
        } catch (NumberFormatException e) {
            showError("Ключ должен быть числом");
            return 1;
        }
    }

    /**
     * Gets the bruteforce file path from text field.
     *
     * @return the file path
     */
    public String getBruteforceFilePath() {
        return bruteforceFilePath.getText().trim();
    }

    /**
     * Gets the analyzed file path from text field.
     *
     * @return the file path
     */
    public String getAnalyzedFilePath() {
        return analyzedFilePath.getText().trim();
    }

    /**
     * Gets the dictionary file path from text field.
     *
     * @return the file path
     */
    public String getDictFilePath() {
        return dictFilePath.getText().trim();
    }

    /**
     * Gets the currently selected file path.
     *
     * @return the file path
     */
    public String getSelectedFilePath() {
        return currentSelectedFile;
    }
}