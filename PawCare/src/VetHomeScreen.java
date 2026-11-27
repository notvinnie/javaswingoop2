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

        petsButton.addActionListener(e -> app.showAllPets());
        recordsButton.addActionListener(e -> app.showRecords());
        ownersButton.addActionListener(e -> app.comingSoon("Pet Owners"));
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