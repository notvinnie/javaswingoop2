import javax.swing.*;
import java.awt.*;

public class OwnerHomeScreen {
    private JPanel mainPanel;
    private JLabel greetingLabel;
    private JButton petsButton, bookButton, billsButton, recordsButton, profileButton, logoutButton;
    private final Main app;

    public OwnerHomeScreen(Main app) {
        this.app = app;
        greetingLabel.setFont(greetingLabel.getFont().deriveFont(Font.BOLD, 20f));

        petsButton.addActionListener(e -> app.comingSoon("My Pets"));
        bookButton.addActionListener(e -> app.comingSoon("Book Appointment"));
        billsButton.addActionListener(e -> app.comingSoon("Medical Bills"));
        recordsButton.addActionListener(e -> app.comingSoon("Medical Records"));
        profileButton.addActionListener(e -> app.showEditProfile());
        logoutButton.addActionListener(e -> app.logout());
    }

    public void refresh() {
        greetingLabel.setText("Hello, " + app.currentUser.fullName + "!");
    }

    public JPanel getPanel() {
        return mainPanel;
    }
}