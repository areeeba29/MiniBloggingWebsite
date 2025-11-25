package ui;

public class MainLauncher {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> new LoginPage().setVisible(true));
    }
}
