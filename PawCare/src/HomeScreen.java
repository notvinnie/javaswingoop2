import javax.swing.*;
import java.awt.*;

public class HomeScreen {
    private JPanel mainPanel;
    private JLabel greetingLabel;
    private JButton petsButton;
    private JButton appointmentButton;
    private JButton remindersButton;
    private JButton logoutButton;

    public HomeScreen(Main app) {
        greetingLabel.setFont(greetingLabel.getFont().deriveFont(Font.BOLD, 20f));

        petsButton.addActionListener(e ->
                JOptionPane.showMessageDialog(mainPanel, "My Pets (coming soon)"));
        appointmentButton.addActionListener(e ->
                JOptionPane.showMessageDialog(mainPanel, "Book Appointment (coming soon)"));
        remindersButton.addActionListener(e ->
                JOptionPane.showMessageDialog(mainPanel, "Reminders (coming soon)"));

        logoutButton.addActionListener(e -> {
            app.currentUser = null;
            app.showScreen("login");
        });
    }

    public void setUser(String username) {
        greetingLabel.setText("Hello, " + username + "!");
    }

    public JPanel getPanel() {
        return mainPanel;
    }
}