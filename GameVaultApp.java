import java.awt.*;
import java.io.IOException;
import javax.swing.*;
import javax.swing.border.*;

public class GameVaultApp extends JFrame {
    private VaultManager manager = new VaultManager();
    
    // UI Colors (Steam Palette)
    private Color steamDark = new Color(23, 26, 33);     // Darkest background
    private Color steamPanel = new Color(27, 40, 56);    // Lighter panel blue
    private Color steamText = new Color(199, 213, 224);  // Light grey text
    private Color steamBlue = new Color(102, 192, 244);   // Highlight blue
    private Color steamButton = new Color(42, 71, 94);   // Button color

    private JComboBox<String> comboId; 
    private JTextField txtTitle = new JTextField(20);
    private JComboBox<String> comboType = new JComboBox<>(new String[]{"Physical", "Digital"});
    private JTextArea displayArea = new JTextArea();

    public GameVaultApp() {
        setTitle("GAME VAULT MANAGER");
        ImageIcon icon = new ImageIcon(".//res//GameVault.png");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(900, 650)); // Made the UI significantly bigger
        getContentPane().setBackground(steamDark);
        setLayout(new BorderLayout(15, 15));

        // --- Header Section ---
        JLabel header = new JLabel("MY GAME LIBRARY", SwingConstants.LEFT);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("SansSerif", Font.BOLD, 28));
        header.setBorder(new EmptyBorder(20, 20, 10, 20));
        add(header, BorderLayout.NORTH);

        // --- Center Section: Library Display ---
        displayArea.setEditable(false);
        displayArea.setBackground(steamDark);
        displayArea.setForeground(steamText);
        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        displayArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(displayArea);
        scrollPane.setBorder(new LineBorder(steamPanel, 2));
        scrollPane.getViewport().setBackground(steamDark);
        
        JPanel centerContainer = new JPanel(new BorderLayout());
        centerContainer.setBackground(steamDark);
        centerContainer.setBorder(new EmptyBorder(0, 20, 0, 10));
        centerContainer.add(scrollPane, BorderLayout.CENTER);
        add(centerContainer, BorderLayout.CENTER);

        // --- Right Section: Sidebar (Inputs & Controls) ---
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(steamPanel);
        sidebar.setPreferredSize(new Dimension(300, 0));
        sidebar.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Setup Dropdowns & Fields
        String[] idOptions = {"G001", "G002", "G003", "G004", "G005", "G006", "G007", "G008", "G009", "G010"};
        comboId = new JComboBox<>(idOptions);
        styleComponent(comboId);
        styleComponent(comboType);
        styleComponent(txtTitle);

        // Adding components to Sidebar
        addSidebarLabel(sidebar, "SELECT GAME ID:");
        sidebar.add(comboId);
        sidebar.add(Box.createRigidArea(new Dimension(0, 15)));
        
        addSidebarLabel(sidebar, "GAME TITLE:");
        sidebar.add(txtTitle);
        sidebar.add(Box.createRigidArea(new Dimension(0, 15)));
        
        addSidebarLabel(sidebar, "FORMAT TYPE:");
        sidebar.add(comboType);
        sidebar.add(Box.createRigidArea(new Dimension(0, 30)));

        // Buttons
        JButton btnInsert = createSteamButton("INSTALL TO VAULT");
        JButton btnUpdate = createSteamButton("UPDATE INFO");
        JButton btnDelete = createSteamButton("UNINSTALL / DELETE");
        JButton btnClear = createSteamButton("CLEAR SELECTION");

        sidebar.add(btnInsert);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnUpdate);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnDelete);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnClear);
        
        add(sidebar, BorderLayout.EAST);

        // --- Footer Section: File Operations ---
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        footer.setBackground(steamDark);
        footer.setBorder(new EmptyBorder(10, 20, 20, 20));

        JButton btnOpen = createSteamButton("OPEN VAULT FILE");
        JButton btnSave = createSteamButton("BACKUP VAULT");
        footer.add(btnOpen);
        footer.add(btnSave);
        add(footer, BorderLayout.SOUTH);

        // --- Event Listeners ---
        btnInsert.addActionListener(e -> {
            try {
                if (txtTitle.getText().isEmpty()) throw new Exception("Title required.");
                manager.insertGame(new PCGame((String)comboId.getSelectedItem(), txtTitle.getText(), (String)comboType.getSelectedItem()));
                refreshDisplay("New game synchronized.");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
        });

        btnUpdate.addActionListener(e -> {
            try {
                manager.updateGame((String)comboId.getSelectedItem(), txtTitle.getText(), (String)comboType.getSelectedItem());
                refreshDisplay("Game details updated.");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
        });

        btnDelete.addActionListener(e -> {
            try {
                manager.deleteGame((String)comboId.getSelectedItem());
                refreshDisplay("Game removed from library.");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
        });

        btnClear.addActionListener(e -> {
            txtTitle.setText("");
            comboId.setSelectedIndex(0);
            comboType.setSelectedIndex(0);
        });

        btnSave.addActionListener(e -> handleFileAction(true));
        btnOpen.addActionListener(e -> handleFileAction(false));

        pack();
        setLocationRelativeTo(null);
    }

    private void styleComponent(JComponent c) {
        c.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        c.setBackground(steamDark);
        c.setForeground(Color.WHITE);
        c.setBorder(new LineBorder(steamBlue, 1));
    }

    private void addSidebarLabel(JPanel panel, String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(steamBlue);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 11));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lbl);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
    }

    private JButton createSteamButton(String text) {
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn.setBackground(steamButton);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(new LineBorder(steamBlue, 1));
        return btn;
    }

    private void handleFileAction(boolean isSave) {
        JFileChooser fc = new JFileChooser();
        int res = isSave ? fc.showSaveDialog(this) : fc.showOpenDialog(this);
        if (res == JFileChooser.APPROVE_OPTION) {
            try {
                if (isSave) manager.saveToFile(fc.getSelectedFile());
                else manager.loadFromFile(fc.getSelectedFile());
                refreshDisplay(isSave ? "Backup complete." : "Library synced from file.");
            } catch (IOException ex) { JOptionPane.showMessageDialog(this, "File error."); }
        }
    }

    private void refreshDisplay(String msg) {
        displayArea.setText("LIBRARY > ALL GAMES\n\n");
        displayArea.append(manager.getAllGamesDisplay());
        displayArea.append("\n\n[STATUS]: " + msg.toUpperCase());
    }
}