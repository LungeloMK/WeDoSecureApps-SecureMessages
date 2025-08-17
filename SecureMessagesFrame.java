package za.ac.tut.ui;
import za.ac.tut.encryption.MessageEncryptor;
import za.ac.tut.message.Message;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
public class SecureMessagesFrame extends JFrame {
    private final JTextArea plainTextArea;
    private final JTextArea maskedTextArea;
    private final MessageEncryptor encryptor;
    private Message currentMessage;
    public SecureMessagesFrame() {
        super("Secure Messages App");
        this.encryptor = new MessageEncryptor();
        this.plainTextArea = new JTextArea(10, 30);
        this.maskedTextArea = new JTextArea(10, 30);
        this.currentMessage = new Message("");  
        initializeUI();
    }
    private void initializeUI() {
        setupTextAreas();
        setupMenu();
        setupWindow();
    }
    private void setupTextAreas() {
    JPanel mainPanel = new JPanel(new BorderLayout());
    JLabel titleLabel = new JLabel("Message Encryptor|", SwingConstants.CENTER);
    titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
    titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));
    mainPanel.add(titleLabel, BorderLayout.NORTH);
    titleLabel.setForeground(Color.BLUE);
        plainTextArea.setBorder(BorderFactory.createTitledBorder("Plain Message"));
    maskedTextArea.setBorder(BorderFactory.createTitledBorder("Masked Message"));
    maskedTextArea.setEditable(false);
    JPanel textPanel = new JPanel(new GridLayout(1, 2, 10, 10));
    textPanel.add(new JScrollPane(plainTextArea));
    textPanel.add(new JScrollPane(maskedTextArea));
    mainPanel.add(textPanel, BorderLayout.CENTER);
    add(mainPanel);
    }
    private void setupMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        addMenuItem(fileMenu, "Open file...", e -> openFile());
        addMenuItem(fileMenu, "Encrypt message...", e -> encryptMessage());
        addMenuItem(fileMenu, "Save masked message...", e -> saveMaskedMessage());
        fileMenu.addSeparator();
        addMenuItem(fileMenu, "Clear", e -> clearTextAreas());
        fileMenu.addSeparator();
        addMenuItem(fileMenu, "Exit", e -> System.exit(0));

        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }
    private void addMenuItem(JMenu menu, String text, ActionListener listener) {
        JMenuItem item = new JMenuItem(text);
        item.addActionListener(listener);
        menu.add(item);
    }
    private void setupWindow() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }
    private void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                String content = readFileContent(fileChooser.getSelectedFile());
                plainTextArea.setText(content);
                currentMessage = new Message(content);
            } catch (IOException ex) {
                showError("File read error");
            }
        }
    }

    private String readFileContent(File file) throws IOException {
        StringBuilder content = new StringBuilder(); 
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }

    private void encryptMessage() {
        currentMessage = new Message(plainTextArea.getText());
        if (currentMessage.getLength() == 0) {
            showWarning("No message to encrypt");
            return;
        }
        maskedTextArea.setText(encryptor.encrypt(currentMessage.getContent()));
    }
    
    private void decryptMessage() {
        if (currentMessage.getLength() == 0) {
            showWarning("No message to decrypt");
            return;
        }
        plainTextArea.setText(encryptor.decrypt(maskedTextArea.getText()));
    }

    private void saveMaskedMessage() {
        if (currentMessage.getLength() == 0) {
            showWarning("No masked message to save");
            return;
        }
        
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileChooser.getSelectedFile()))) {
                writer.write(maskedTextArea.getText());
            } catch (IOException ex) {
                showError("File save error");
            }
        }
    }

    private void clearTextAreas() {
        plainTextArea.setText("");
        maskedTextArea.setText("");
        currentMessage = new Message("");
    }

    private void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "Warning", JOptionPane.WARNING_MESSAGE);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}