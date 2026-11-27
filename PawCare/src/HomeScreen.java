import javax.swing.*;

public class HomeScreen {
    private JPanel mainPanel;
    private JButton logoutButton;

    public HomeScreen(Main app) {
        logoutButton.addActionListener(e -> app.showScreen("login"));
    }

    public JPanel getPanel() {
        return mainPanel;
    }
}