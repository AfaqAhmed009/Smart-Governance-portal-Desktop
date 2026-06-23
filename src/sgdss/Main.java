package sgdss;

import javax.swing.*;

/**
 * Main entry point for the SGDSS application.
 * Initializes the application context and displays the login frame.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            Theme.configureUIManager();
            AppContext ctx = new AppContext();
            new LoginFrame(ctx).setVisible(true);
        });
    }
}
