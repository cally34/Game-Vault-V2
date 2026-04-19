import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Ensures the UI is created on the Event Dispatch Thread for thread safety
        SwingUtilities.invokeLater(() -> {
            GameVaultApp app = new GameVaultApp();
            app.setVisible(true);
        });
    }
}