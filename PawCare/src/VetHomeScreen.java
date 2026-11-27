import javax.swing.*;
import java.awt.*;

public class VetHomeScreen {
    private JPanel mainPanel;
    private JLabel greetingLabel;
    private JButton petsButton, recordsButton, ownersButton, profileButton, logoutButton;
    private final Main app;

    public VetHomeScreen(Main app) {
        this.app = app;
        greetingLabel.setFont(greetingLabel.getFont().deriveFont(Font.BOLD, 20f));

        petsButton.addActionListener(e -> app.comingSoon("All Pets"));
        recordsButton.addActionListener(e -> app.comingSoon("All Medical Records"));
        ownersButton.addActionListener(e -> app.comingSoon("Pet Owners"));
        profileButton.addActionListener(e -> app.comingSoon("Edit Profile"));
        logoutButton.addActionListener(e -> app.logout());
    }

    public void refresh() {
        greetingLabel.setText("Hello, " + app.currentUser.fullName + "!");
    }

    public JPanel getPanel() {
        return mainPanel;
    }
}